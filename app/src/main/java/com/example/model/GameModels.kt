package com.example.model

enum class GemType(val id: Int, val nameKey: String, val iconName: String) {
  RUBY(0, "ruby", "favorite"),
  SAPPHIRE(1, "sapphire", "diamond"),
  EMERALD(2, "emerald", "hexagon"),
  TOPAZ(3, "topaz", "star"),
  AMETHYST(4, "amethyst", "change_history")
}

data class PlayerProfile(
  val name: String = "Aether_Seeker",
  val title: String = "Crystal Master • Realm 4",
  val level: Int = 24,
  val gold: Int = 0,
  val gems: Int = 0,
  val xp: Int = 7450,
  val maxXp: Int = 10000,
  val hammersLeft: Int = 1,
  val shufflesLeft: Int = 2,
  val rainbowsLeft: Int = 1,
  val totalScore: Int = 0,
  val isLoggedIn: Boolean = true,
  val email: String = "seeker@mysticmatch.realm",
  val avatar: String = "🧙‍♂️"
)

data class Level(
  val id: Int,
  val title: String,
  val subtitle: String,
  val stars: Int = 0,
  val unlocked: Boolean = false,
  val bestScore: Int = 0,
  val recommendedPower: Int = 1100,
  val reward: String = "Rare Crystal x3"
)

data class Quest(
  val title: String = "Daily Quest: Prism Collector",
  val description: String = "Harvest 3 Legendary Shards from the Obsidian Caverns to claim your bonus reward.",
  val current: Int = 2,
  val total: Int = 3,
  val reward: String = "+500 Gold & 10 Gems",
  val claimed: Boolean = false
)
