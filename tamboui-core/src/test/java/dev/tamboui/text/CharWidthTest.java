/*
 * Copyright (c) 2025 TamboUI Contributors
 * SPDX-License-Identifier: MIT
 */
package dev.tamboui.text;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CharWidthTest {

    @Test
    @DisplayName("ASCII characters have width 1")
    void asciiWidth() {
        assertThat(CharWidth.of('A')).isEqualTo(1);
        assertThat(CharWidth.of('z')).isEqualTo(1);
        assertThat(CharWidth.of('0')).isEqualTo(1);
        assertThat(CharWidth.of(' ')).isEqualTo(1);
        assertThat(CharWidth.of('!')).isEqualTo(1);
    }

    @Test
    @DisplayName("CJK Unified Ideographs have width 2")
    void cjkWidth() {
        // U+4E16 = 世
        assertThat(CharWidth.of(0x4E16)).isEqualTo(2);
        // U+754C = 界
        assertThat(CharWidth.of(0x754C)).isEqualTo(2);
        // U+4E00 = first CJK ideograph
        assertThat(CharWidth.of(0x4E00)).isEqualTo(2);
        // U+9FFF = last in CJK Unified Ideographs
        assertThat(CharWidth.of(0x9FFF)).isEqualTo(2);
    }

    @Test
    @DisplayName("Hiragana and Katakana have width 2")
    void japaneseKanaWidth() {
        // U+3053 = こ
        assertThat(CharWidth.of(0x3053)).isEqualTo(2);
        // U+30F3 = ン
        assertThat(CharWidth.of(0x30F3)).isEqualTo(2);
    }

    @Test
    @DisplayName("Hangul Syllables have width 2")
    void hangulWidth() {
        // U+AC00 = first Hangul syllable (가)
        assertThat(CharWidth.of(0xAC00)).isEqualTo(2);
        // U+D7AF = last Hangul syllable
        assertThat(CharWidth.of(0xD7AF)).isEqualTo(2);
    }

    @Test
    @DisplayName("Fullwidth forms have width 2")
    void fullwidthWidth() {
        // U+FF01 = Fullwidth exclamation mark
        assertThat(CharWidth.of(0xFF01)).isEqualTo(2);
        // U+FF21 = Fullwidth Latin A
        assertThat(CharWidth.of(0xFF21)).isEqualTo(2);
    }

    @Test
    @DisplayName("Common emoji have width 2")
    void emojiWidth() {
        // U+1F525 = 🔥
        assertThat(CharWidth.of(0x1F525)).isEqualTo(2);
        // U+1F389 = 🎉
        assertThat(CharWidth.of(0x1F389)).isEqualTo(2);
        // U+1F600 = 😀
        assertThat(CharWidth.of(0x1F600)).isEqualTo(2);
        // U+1F680 = 🚀
        assertThat(CharWidth.of(0x1F680)).isEqualTo(2);
    }

    @Test
    @DisplayName("Combining marks have width 0")
    void combiningMarksWidth() {
        // U+0300 = Combining grave accent
        assertThat(CharWidth.of(0x0300)).isEqualTo(0);
        // U+0301 = Combining acute accent
        assertThat(CharWidth.of(0x0301)).isEqualTo(0);
        // U+0302 = Combining circumflex accent
        assertThat(CharWidth.of(0x0302)).isEqualTo(0);
    }

    @Test
    @DisplayName("Zero-width joiner has width 0")
    void zwjWidth() {
        // U+200D = Zero-width joiner
        assertThat(CharWidth.of(0x200D)).isEqualTo(0);
    }

    @Test
    @DisplayName("Zero-width space has width 0")
    void zwsWidth() {
        // U+200B = Zero-width space
        assertThat(CharWidth.of(0x200B)).isEqualTo(0);
    }

    @Test
    @DisplayName("Variation selectors have width 0")
    void variationSelectorsWidth() {
        // U+FE0F = Variation Selector-16 (emoji presentation)
        assertThat(CharWidth.of(0xFE0F)).isEqualTo(0);
        // U+FE0E = Variation Selector-15 (text presentation)
        assertThat(CharWidth.of(0xFE0E)).isEqualTo(0);
    }

    @Test
    @DisplayName("String width sums individual code point widths")
    void stringWidth() {
        // "Hello" = 5 * 1 = 5
        assertThat(CharWidth.of("Hello")).isEqualTo(5);
        // "世界" = 2 * 2 = 4
        assertThat(CharWidth.of("世界")).isEqualTo(4);
    }

    @Test
    @DisplayName("Mixed ASCII and CJK string width")
    void mixedStringWidth() {
        // "Hi世界" = 2*1 + 2*2 = 6
        assertThat(CharWidth.of("Hi世界")).isEqualTo(6);
    }

    @Test
    @DisplayName("String with emoji has correct width")
    void emojiStringWidth() {
        // Each emoji is width 2, encoded as surrogate pair in UTF-16
        // 🔥 = U+1F525, width 2
        assertThat(CharWidth.of("\uD83D\uDD25")).isEqualTo(2);
        // "A🔥B" = 1 + 2 + 1 = 4
        assertThat(CharWidth.of("A\uD83D\uDD25B")).isEqualTo(4);
    }

    @Test
    @DisplayName("Null and empty string have width 0")
    void nullAndEmptyWidth() {
        assertThat(CharWidth.of((String) null)).isEqualTo(0);
        assertThat(CharWidth.of("")).isEqualTo(0);
    }

    @Test
    @DisplayName("substringByWidth clips at width boundary")
    void substringByWidth() {
        // "Hello" clipped to width 3 = "Hel"
        assertThat(CharWidth.substringByWidth("Hello", 3)).isEqualTo("Hel");
        // "世界好" clipped to width 4 = "世界" (each char is width 2)
        assertThat(CharWidth.substringByWidth("世界好", 4)).isEqualTo("世界");
        // "世界好" clipped to width 5 = "世界" (好 would need 2 more, total 6 > 5)
        assertThat(CharWidth.substringByWidth("世界好", 5)).isEqualTo("世界");
    }

    @Test
    @DisplayName("substringByWidth with mixed content")
    void substringByWidthMixed() {
        // "A世B" = widths [1, 2, 1], total 4
        // Clipped to width 2 = "A" (世 needs 2 more = 3 > 2)
        assertThat(CharWidth.substringByWidth("A世B", 2)).isEqualTo("A");
        // Clipped to width 3 = "A世"
        assertThat(CharWidth.substringByWidth("A世B", 3)).isEqualTo("A世");
    }

    @Test
    @DisplayName("substringByWidth with zero width returns empty")
    void substringByWidthZero() {
        assertThat(CharWidth.substringByWidth("Hello", 0)).isEqualTo("");
        assertThat(CharWidth.substringByWidth(null, 5)).isEqualTo("");
    }

    @Test
    @DisplayName("substringByWidth respects surrogate pair boundaries")
    void substringByWidthSurrogatePairs() {
        // 🔥 (U+1F525) takes 2 columns, is a surrogate pair in Java
        // "🔥A" clipped to width 1: fire doesn't fit, result is empty
        assertThat(CharWidth.substringByWidth("\uD83D\uDD25A", 1)).isEqualTo("");
        // "🔥A" clipped to width 2: fire fits
        assertThat(CharWidth.substringByWidth("\uD83D\uDD25A", 2)).isEqualTo("\uD83D\uDD25");
        // "🔥A" clipped to width 3: both fit
        assertThat(CharWidth.substringByWidth("\uD83D\uDD25A", 3)).isEqualTo("\uD83D\uDD25A");
    }

    @Test
    @DisplayName("substringByWidthFromEnd returns suffix within width")
    void substringByWidthFromEnd() {
        // "Hello" from end, width 3 = "llo"
        assertThat(CharWidth.substringByWidthFromEnd("Hello", 3)).isEqualTo("llo");
        // "世界好" from end, width 4 = "界好"
        assertThat(CharWidth.substringByWidthFromEnd("世界好", 4)).isEqualTo("界好");
    }

    @Test
    @DisplayName("substringByWidthFromEnd with mixed content")
    void substringByWidthFromEndMixed() {
        // "A世B" from end, width 3 = "世B"
        assertThat(CharWidth.substringByWidthFromEnd("A世B", 3)).isEqualTo("世B");
    }

    @Test
    @DisplayName("Emoji_Presentation symbols have width 2")
    void emojiPresentationWidth() {
        // U+2614 = ☔ (Umbrella with Rain Drops) - Emoji_Presentation + EAW=W
        assertThat(CharWidth.of(0x2614)).isEqualTo(2);
        // U+26A1 = ⚡ (High Voltage) - Emoji_Presentation
        assertThat(CharWidth.of(0x26A1)).isEqualTo(2);
        // U+2B50 = ⭐ (Star) - Emoji_Presentation + EAW=W
        assertThat(CharWidth.of(0x2B50)).isEqualTo(2);
        // U+2705 = ✅ (Check Mark) - Emoji_Presentation + EAW=W
        assertThat(CharWidth.of(0x2705)).isEqualTo(2);
    }

    @Test
    @DisplayName("Text-presentation symbols have width 1")
    void textPresentationWidth() {
        // U+2600 = ☀ (Sun) - no Emoji_Presentation, text mode = width 1
        assertThat(CharWidth.of(0x2600)).isEqualTo(1);
        // U+2764 = ❤ (Heart) - no Emoji_Presentation, text mode = width 1
        assertThat(CharWidth.of(0x2764)).isEqualTo(1);
        // U+2702 = ✂ (Scissors) - no Emoji_Presentation
        assertThat(CharWidth.of(0x2702)).isEqualTo(1);
        // U+2660 = ♠ (Spade) - no Emoji_Presentation
        assertThat(CharWidth.of(0x2660)).isEqualTo(1);
    }

    @Test
    @DisplayName("CJK Extension B characters have width 2")
    void cjkExtBWidth() {
        // U+20000 = first CJK Extension B character
        assertThat(CharWidth.of(0x20000)).isEqualTo(2);
    }

    @Test
    @DisplayName("Regular Latin extended characters have width 1")
    void latinExtendedWidth() {
        // U+00E9 = é
        assertThat(CharWidth.of(0x00E9)).isEqualTo(1);
        // U+00F1 = ñ
        assertThat(CharWidth.of(0x00F1)).isEqualTo(1);
    }

    @Test
    @DisplayName("String with combining marks does not add width")
    void combiningMarksInString() {
        // "e" + combining acute = "é" but width 1
        assertThat(CharWidth.of("e\u0301")).isEqualTo(1);
        // "a" + combining diaeresis = "ä" but width 1
        assertThat(CharWidth.of("a\u0308")).isEqualTo(1);
    }

    @Test
    @DisplayName("ZWJ family emoji has width 2")
    void zwjFamilyEmojiHasWidth2() {
        // 👨‍👦 = man (U+1F468) + ZWJ (U+200D) + boy (U+1F466)
        String family = "\uD83D\uDC68\u200D\uD83D\uDC66";
        assertThat(CharWidth.of(family)).isEqualTo(2);
    }

    @Test
    @DisplayName("ZWJ farmer emoji has width 2")
    void zwjFarmerEmojiHasWidth2() {
        // 🧑‍🌾 = person (U+1F9D1) + ZWJ (U+200D) + sheaf of rice (U+1F33E)
        String farmer = "\uD83E\uDDD1\u200D\uD83C\uDF3E";
        assertThat(CharWidth.of(farmer)).isEqualTo(2);
    }

    @Test
    @DisplayName("Flag emoji has width 2")
    void flagEmojiHasWidth2() {
        // 🇫🇷 = Regional Indicator F (U+1F1EB) + Regional Indicator R (U+1F1F7)
        String france = "\uD83C\uDDEB\uD83C\uDDF7";
        assertThat(CharWidth.of(france)).isEqualTo(2);
    }

    @Test
    @DisplayName("Greenland flag emoji has width 2")
    void greenlandFlagHasWidth2() {
        // 🇬🇱 = Regional Indicator G (U+1F1EC) + Regional Indicator L (U+1F1F1)
        String greenland = "\uD83C\uDDEC\uD83C\uDDF1";
        assertThat(CharWidth.of(greenland)).isEqualTo(2);
    }

    @Test
    @DisplayName("Skin tone modifier emoji has width 2")
    void skinToneEmojiHasWidth2() {
        // 👋🏻 = waving hand (U+1F44B) + light skin tone (U+1F3FB)
        String wave = "\uD83D\uDC4B\uD83C\uDFFB";
        assertThat(CharWidth.of(wave)).isEqualTo(2);
    }

    @Test
    @DisplayName("ASCII string fast path")
    void asciiStringFastPath() {
        // Should not engage grapheme logic
        assertThat(CharWidth.of("Hello World")).isEqualTo(11);
    }

    @Test
    @DisplayName("Multiple flag emoji have correct width")
    void multipleFlagsHaveCorrectWidth() {
        // 🇫🇷🇬🇱 = France + Greenland = 2 + 2 = 4
        String flags = "\uD83C\uDDEB\uD83C\uDDF7\uD83C\uDDEC\uD83C\uDDF1";
        assertThat(CharWidth.of(flags)).isEqualTo(4);
    }

    @Test
    @DisplayName("Complex ZWJ sequence has width 2")
    void complexZwjSequenceHasWidth2() {
        // 👨‍👩‍👧‍👦 = man + ZWJ + woman + ZWJ + girl + ZWJ + boy
        String family = "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66";
        assertThat(CharWidth.of(family)).isEqualTo(2);
    }

    @Test
    @DisplayName("bald_man ZWJ emoji has width 2")
    void baldManZwjEmojiHasWidth2() {
        // 👨‍🦲 = man (U+1F468) + ZWJ (U+200D) + bald (U+1F9B2)
        String baldMan = "\uD83D\uDC68\u200D\uD83E\uDDB2";
        assertThat(CharWidth.of(baldMan)).isEqualTo(2);
    }

    @Test
    @DisplayName("bald emoji component has width 2")
    void baldEmojiComponentHasWidth2() {
        // 🦲 = U+1F9B2 (standalone bald emoji)
        assertThat(CharWidth.of(0x1F9B2)).isEqualTo(2);
    }
}
