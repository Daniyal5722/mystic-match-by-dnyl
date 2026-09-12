package com.example.model

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.audio.SoundManager
import kotlin.math.abs

class GameViewModel(application: Application) : AndroidViewModel(application) {
  private val sharedPrefs = application.getSharedPreferences("mystic_match_prefs", Context.MODE_PRIVATE)
  private val soundManager = SoundManager()

  var currentScreen by mutableStateOf("home")
    private set

  var profile by mutableStateOf(PlayerProfile())
    private set

  var levels by mutableStateOf(
    listOf(
      Level(1, "Stage 1: Awakening", "Forest Edge", 0, true, 0),
      Level(2, "Stage 2: Broken Bridge", "River Ruins", 0, false, 0),
      Level(3, "Stage 3: Sunken Shrine", "Ancient Vault", 0, false, 0),
      Level(4, "Stage 4: Mossy Gorge", "Canyon Pass", 0, false, 0),
      Level(5, "Stage 5: Whispering Winds", "Sky Summit", 0, false, 0),
      Level(6, "Stage 6: Crystal Cavern", "Underground", 0, false, 0),
      Level(7, "Stage 7: Magma Core", "Volcano Edge", 0, false, 0),
      Level(8, "Stage 8: Aether Gateway", "Cloud Sanctum", 0, false, 0)
    )
  )
    private set

  var activeLevel by mutableStateOf<Level?>(null)
    private set

  var isPaused by mutableStateOf(false)

  // Board state: 8x8 grid of GemType
  var board by mutableStateOf(List(8) { List(8) { GemType.values().random() } })
    private set

  var levelScore by mutableStateOf(0)
  var movesLeft by mutableStateOf(25)
  var sapphiresCollected by mutableStateOf(12)
  val targetSapphires = 40

  var selectedGem by mutableStateOf<Pair<Int, Int>?>(null)
  var activeBooster by mutableStateOf<String?>(null)
  var isAnimating by mutableStateOf(false)

  var gameModalState by mutableStateOf<String?>(null) // "win", "lose", null
  var selectedLevelForModal by mutableStateOf<Level?>(null)
  var isBossModalOpen by mutableStateOf(false)

  // Combo Multipliers
  var currentChain by mutableStateOf(1)
  var activeMultiplierText by mutableStateOf<String?>(null)

  // Settings
  var musicEnabled by mutableStateOf(true)
  var sfxEnabled by mutableStateOf(true)
  var vibrationEnabled by mutableStateOf(true)

  var dailyClaimed by mutableStateOf(false)
    private set

  var questClaimed by mutableStateOf(false)
    private set

  init {
    loadProgress()
    initNewBoard()
  }

  fun navigateTo(screen: String) {
    currentScreen = screen
    if (sfxEnabled) {
      soundManager.playMenuClickSound()
    }
  }

  fun openLevelModal(level: Level) {
    selectedLevelForModal = level
  }

  fun closeLevelModal() {
    selectedLevelForModal = null
  }

  fun openBossModal() {
    isBossModalOpen = true
  }

  fun closeBossModal() {
    isBossModalOpen = false
  }

  fun resetLevelScore() {
    levelScore = 0
  }

  fun startLevel(level: Level) {
    if (sfxEnabled) {
      soundManager.playMenuClickSound()
    }
    activeLevel = level
    levelScore = 0
    movesLeft = 25
    sapphiresCollected = 0
    gameModalState = null
    selectedLevelForModal = null
    initNewBoard()
    currentScreen = "game"
  }

  fun initNewBoard() {
    var newBoard: List<List<GemType>>
    do {
      newBoard = List(8) { List(8) { GemType.values().random() } }
    } while (checkHasInitialMatches(newBoard))
    board = newBoard
  }

  private fun checkHasInitialMatches(grid: List<List<GemType>>): Boolean {
    // Check horizontal
    for (r in 0 until 8) {
      for (c in 0 until 6) {
        if (grid[r][c] == grid[r][c+1] && grid[r][c] == grid[r][c+2]) return true
      }
    }
    // Check vertical
    for (r in 0 until 6) {
      for (c in 0 until 8) {
        if (grid[r][c] == grid[r+1][c] && grid[r][c] == grid[r+2][c]) return true
      }
    }
    return false
  }

  fun onGemClick(r: Int, c: Int) {
    if (isAnimating || gameModalState != null || isPaused) return

    if (activeBooster == "hammer") {
      useHammer(r, c)
      return
    }

    val sel = selectedGem
    if (sel == null) {
      selectedGem = Pair(r, c)
    } else {
      val (r1, c1) = sel
      val r2 = r
      val c2 = c

      val isAdjacent = (abs(r1 - r2) == 1 && c1 == c2) || (abs(c1 - c2) == 1 && r1 == r2)
      if (isAdjacent) {
        // Swap
        swapGems(r1, c1, r2, c2)
        selectedGem = null

        // Check match
        val matches = findAllMatches(board)
        if (matches.isNotEmpty()) {
          movesLeft--
          currentChain = 1
          processMatches(matches)
        } else {
          // Swap back
          swapGems(r1, c1, r2, c2)
        }
      } else {
        selectedGem = Pair(r, c)
      }
    }
  }

  private fun swapGems(r1: Int, c1: Int, r2: Int, c2: Int) {
    val mutableBoard = board.map { it.toMutableList() }.toMutableList()
    val temp = mutableBoard[r1][c1]
    mutableBoard[r1][c1] = mutableBoard[r2][c2]
    mutableBoard[r2][c2] = temp
    board = mutableBoard
  }

  private fun findAllMatches(grid: List<List<GemType>>): Set<Pair<Int, Int>> {
    val matchSet = mutableSetOf<Pair<Int, Int>>()

    // Horizontal
    for (r in 0 until 8) {
      for (c in 0 until 6) {
        val t = grid[r][c]
        if (t == grid[r][c+1] && t == grid[r][c+2]) {
          matchSet.add(Pair(r, c))
          matchSet.add(Pair(r, c+1))
          matchSet.add(Pair(r, c+2))
        }
      }
    }

    // Vertical
    for (r in 0 until 6) {
      for (c in 0 until 8) {
        val t = grid[r][c]
        if (t == grid[r+1][c] && t == grid[r+2][c]) {
          matchSet.add(Pair(r, c))
          matchSet.add(Pair(r+1, c))
          matchSet.add(Pair(r+2, c))
        }
      }
    }

    return matchSet
  }

  private fun processMatches(matches: Set<Pair<Int, Int>>) {
    if (sfxEnabled) {
      soundManager.playMatchSound()
    }
    isAnimating = true
    val mutableBoard = board.map { it.toMutableList() }.toMutableList()
    var sapphireCount = 0

    for ((r, c) in matches) {
      if (mutableBoard[r][c] == GemType.SAPPHIRE) {
        sapphireCount++
      }
    }

    val matchSizeMult = if (matches.size > 3) (matches.size - 2) else 1
    val finalMult = matchSizeMult * currentChain
    val basePoints = matches.size * 50
    val pts = basePoints * finalMult

    if (finalMult > 1) {
      activeMultiplierText = "x$finalMult MULTIPLIER!"
    } else {
      activeMultiplierText = null
    }

    levelScore += pts
    val goldEarned = matches.size * 10 * finalMult
    val gemsEarned = if (matches.size >= 4) (matches.size - 2) * currentChain else 1
    profile = profile.copy(
      totalScore = profile.totalScore + pts,
      gold = profile.gold + goldEarned,
      gems = profile.gems + gemsEarned
    )
    sharedPrefs.edit()
      .putInt("total_score", profile.totalScore)
      .putInt("user_gold", profile.gold)
      .putInt("user_gems", profile.gems)
      .apply()
    sapphiresCollected += sapphireCount

    // Apply gravity & refill
    applyGravityAndRefill(matches)
  }

  private fun applyGravityAndRefill(matched: Set<Pair<Int, Int>>) {
    val mutableBoard = board.map { it.toMutableList() }.toMutableList()

    // Clear matched
    for ((r, c) in matched) {
      mutableBoard[r][c] = GemType.values().random() // simplified cascade refill
    }

    board = mutableBoard

    // Check if cascade creates further matches
    val nextMatches = findAllMatches(board)
    if (nextMatches.isNotEmpty()) {
      currentChain++
      // chain reaction
      android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
        processMatches(nextMatches)
      }, 450)
    } else {
      isAnimating = false
      // Clear multiplier after brief delay
      android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
        activeMultiplierText = null
      }, 1200)
      checkGameConditions()
    }
  }

  private fun checkGameConditions() {
    if (sapphiresCollected >= targetSapphires) {
      gameModalState = "win"
      if (sfxEnabled) {
        soundManager.playLevelCompleteSound()
      }
      saveLevelProgress(3)
    } else if (movesLeft <= 0) {
      gameModalState = "lose"
    }
  }

  fun activateBooster(type: String) {
    when (type) {
      "shuffle" -> {
        initNewBoard()
        profile = profile.copy(shufflesLeft = (profile.shufflesLeft - 1).coerceAtLeast(0))
      }
      "hammer" -> {
        activeBooster = "hammer"
      }
      "rainbow" -> {
        // Clear center area
        val mutableBoard = board.map { it.toMutableList() }.toMutableList()
        for (r in 3..4) {
          for (c in 3..4) {
            mutableBoard[r][c] = GemType.values().random()
          }
        }
        board = mutableBoard
        val pts = 500
        levelScore += pts
        profile = profile.copy(
          totalScore = profile.totalScore + pts,
          rainbowsLeft = (profile.rainbowsLeft - 1).coerceAtLeast(0)
        )
      }
    }
  }

  private fun useHammer(r: Int, c: Int) {
    val mutableBoard = board.map { it.toMutableList() }.toMutableList()
    mutableBoard[r][c] = GemType.values().random()
    board = mutableBoard
    activeBooster = null
    profile = profile.copy(hammersLeft = (profile.hammersLeft - 1).coerceAtLeast(0))
  }

  fun saveLevelProgress(earnedStars: Int) {
    val levelId = activeLevel?.id ?: 1
    val bonusGold = 300
    val bonusGems = 15
    profile = profile.copy(
      gold = profile.gold + bonusGold,
      gems = profile.gems + bonusGems
    )
    levels = levels.map { lvl ->
      if (lvl.id == levelId) {
        lvl.copy(stars = maxOf(lvl.stars, earnedStars), bestScore = maxOf(lvl.bestScore, levelScore))
      } else if (lvl.id == levelId + 1) {
        lvl.copy(unlocked = true)
      } else {
        lvl
      }
    }
    // Save to shared prefs
    val editor = sharedPrefs.edit()
    editor.putInt("level_${levelId}_stars", earnedStars)
    editor.putInt("level_${levelId}_score", levelScore)
    editor.putInt("total_score", profile.totalScore)
    editor.putInt("user_gold", profile.gold)
    editor.putInt("user_gems", profile.gems)
    editor.putBoolean("level_${levelId + 1}_unlocked", true)
    editor.apply()
  }

  fun loginUser(username: String, emailInput: String, selectedAvatar: String) {
    if (username.isNotBlank()) {
      profile = profile.copy(
        name = username.trim(),
        email = if (emailInput.isBlank()) "${username.lowercase().replace(" ", "")}@mysticmatch.realm" else emailInput.trim(),
        avatar = selectedAvatar,
        isLoggedIn = true
      )
      currentScreen = "home"
      sharedPrefs.edit()
        .putString("user_name", profile.name)
        .putString("user_email", profile.email)
        .putString("user_avatar", profile.avatar)
        .putBoolean("user_is_logged_in", true)
        .apply()
    }
  }

  fun logoutUser() {
    profile = PlayerProfile(
      isLoggedIn = false,
      gold = 0,
      gems = 0,
      totalScore = 0
    )
    dailyClaimed = false
    questClaimed = false
    currentScreen = "login"
    sharedPrefs.edit()
      .clear()
      .putBoolean("user_is_logged_in", false)
      .apply()
  }

  fun claimDailyReward() {
    if (!dailyClaimed) {
      dailyClaimed = true
      if (sfxEnabled) {
        soundManager.playLevelCompleteSound()
      }
      profile = profile.copy(
        gold = profile.gold + 500,
        gems = profile.gems + 10
      )
      sharedPrefs.edit()
        .putInt("user_gold", profile.gold)
        .putInt("user_gems", profile.gems)
        .putBoolean("daily_reward_claimed", true)
        .apply()
    }
  }

  fun claimQuestReward() {
    if (!questClaimed) {
      questClaimed = true
      if (sfxEnabled) {
        soundManager.playLevelCompleteSound()
      }
      profile = profile.copy(
        gold = profile.gold + 300,
        gems = profile.gems + 5
      )
      sharedPrefs.edit()
        .putInt("user_gold", profile.gold)
        .putInt("user_gems", profile.gems)
        .putBoolean("quest_reward_claimed", true)
        .apply()
    }
  }

  private fun loadProgress() {
    val total = sharedPrefs.getInt("total_score", 0)
    val gold = sharedPrefs.getInt("user_gold", 0)
    val gems = sharedPrefs.getInt("user_gems", 0)
    val isLoggedIn = sharedPrefs.getBoolean("user_is_logged_in", true)
    val name = sharedPrefs.getString("user_name", "Aether_Seeker") ?: "Aether_Seeker"
    val email = sharedPrefs.getString("user_email", "seeker@mysticmatch.realm") ?: "seeker@mysticmatch.realm"
    val avatar = sharedPrefs.getString("user_avatar", "🧙‍♂️") ?: "🧙‍♂️"
    
    val dailyClaimedVal = sharedPrefs.getBoolean("daily_reward_claimed", false)
    val questClaimedVal = sharedPrefs.getBoolean("quest_reward_claimed", false)
    this.dailyClaimed = dailyClaimedVal
    this.questClaimed = questClaimedVal

    val loadedLevels = levels.map { lvl ->
      val stars = sharedPrefs.getInt("level_${lvl.id}_stars", 0)
      val score = sharedPrefs.getInt("level_${lvl.id}_score", 0)
      val unlocked = if (lvl.id == 1) true else sharedPrefs.getBoolean("level_${lvl.id}_unlocked", false)
      lvl.copy(stars = stars, bestScore = score, unlocked = unlocked)
    }
    levels = loadedLevels
    profile = profile.copy(
      totalScore = total,
      gold = gold,
      gems = gems,
      isLoggedIn = isLoggedIn,
      name = name,
      email = email,
      avatar = avatar
    )
    currentScreen = if (isLoggedIn) "home" else "login"
  }

  fun toggleMusic() { musicEnabled = !musicEnabled }
  fun toggleSfx() { sfxEnabled = !sfxEnabled }
  fun toggleVibration() { vibrationEnabled = !vibrationEnabled }
}
