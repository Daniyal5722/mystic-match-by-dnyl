package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

class SoundManager {
  private val sampleRate = 22050
  private val scope = CoroutineScope(Dispatchers.Default)

  private fun playTone(
    frequencies: List<Float>,
    durationMs: Int,
    amplitude: Float = 0.4f,
    sweep: Boolean = false
  ) {
    scope.launch {
      try {
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val sample = FloatArray(numSamples)

        for (i in 0 until numSamples) {
          val t = i.toFloat() / sampleRate

          if (frequencies.size == 1) {
            val freq = if (sweep) {
              frequencies[0] + (i.toFloat() / numSamples) * frequencies[0]
            } else {
              frequencies[0]
            }
            sample[i] = (sin(2.0 * Math.PI * freq * t) * amplitude).toFloat()
          } else if (frequencies.size > 1) {
            val noteIndex = (t / (durationMs / 1000.0f) * frequencies.size).toInt().coerceIn(0, frequencies.size - 1)
            val freq = frequencies[noteIndex]
            sample[i] = (sin(2.0 * Math.PI * freq * t) * amplitude).toFloat()
          }

          // Linear fade-out (prevent pops)
          if (i > numSamples - 1000) {
            val fadeFactor = (numSamples - i).toFloat() / 1000f
            sample[i] *= fadeFactor
          }
          // Linear fade-in
          if (i < 500) {
            val fadeFactor = i.toFloat() / 500f
            sample[i] *= fadeFactor
          }
        }

        val audioTrack = AudioTrack.Builder()
          .setAudioAttributes(
            AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_GAME)
              .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
              .build()
          )
          .setAudioFormat(
            AudioFormat.Builder()
              .setEncoding(AudioFormat.ENCODING_PCM_FLOAT)
              .setSampleRate(sampleRate)
              .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
              .build()
          )
          .setBufferSizeInBytes(sample.size * 4)
          .setTransferMode(AudioTrack.MODE_STATIC)
          .build()

        audioTrack.write(sample, 0, sample.size, AudioTrack.WRITE_NON_BLOCKING)
        audioTrack.play()

        scope.launch {
          kotlinx.coroutines.delay(durationMs + 100L)
          try {
            audioTrack.stop()
            audioTrack.release()
          } catch (ignored: Exception) {}
        }
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  fun playMatchSound() {
    playTone(listOf(580f), 120, amplitude = 0.25f, sweep = true)
  }

  fun playLevelCompleteSound() {
    playTone(listOf(261.63f, 329.63f, 392.00f, 523.25f), 550, amplitude = 0.35f)
  }

  fun playMenuClickSound() {
    playTone(listOf(160f), 40, amplitude = 0.15f)
  }
}
