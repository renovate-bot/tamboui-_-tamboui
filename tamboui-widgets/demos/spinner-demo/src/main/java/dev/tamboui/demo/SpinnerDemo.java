///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS dev.tamboui:tamboui-widgets:LATEST
//DEPS dev.tamboui:tamboui-jline3-backend:LATEST

/*
 * Copyright TamboUI Contributors
 * SPDX-License-Identifier: MIT
 */
package dev.tamboui.demo;

import java.util.ArrayList;
import java.util.List;

import dev.tamboui.layout.Constraint;
import dev.tamboui.layout.Layout;
import dev.tamboui.layout.Rect;
import dev.tamboui.style.Color;
import dev.tamboui.style.Style;
import dev.tamboui.terminal.Backend;
import dev.tamboui.terminal.BackendFactory;
import dev.tamboui.terminal.Frame;
import dev.tamboui.terminal.Terminal;
import dev.tamboui.text.Line;
import dev.tamboui.text.Span;
import dev.tamboui.text.Text;
import dev.tamboui.widgets.block.Block;
import dev.tamboui.widgets.block.BorderType;
import dev.tamboui.widgets.block.Borders;
import dev.tamboui.widgets.block.Title;
import dev.tamboui.widgets.paragraph.Paragraph;
import dev.tamboui.widgets.spinner.Spinner;
import dev.tamboui.widgets.spinner.SpinnerFrameSet;
import dev.tamboui.widgets.spinner.SpinnerState;
import dev.tamboui.widgets.spinner.SpinnerStyle;

/**
 * Demo TUI application showcasing the Spinner widget.
 * <p>
 * Demonstrates all built-in spinner styles including braille-based patterns.
 */
public class SpinnerDemo {

    private static final Color[] COLORS = {
        Color.rgb(0, 180, 216),   // cyan
        Color.rgb(46, 204, 113),  // green
        Color.rgb(241, 196, 15),  // yellow
        Color.rgb(155, 89, 182),  // magenta
        Color.rgb(52, 152, 219),  // blue
        Color.rgb(231, 76, 60),   // red
        Color.rgb(230, 126, 34),  // orange
    };

    /** Custom frame set example. */
    private static final SpinnerFrameSet CUSTOM_FRAMES = SpinnerFrameSet.of(
            "\u25d0", "\u25d3", "\u25d1", "\u25d2"  // rotating half-circle
    );

    private boolean running = true;
    private long frameCount;

    private record SpinnerEntry(String label, Spinner spinner, SpinnerState state) { }

    private final List<SpinnerEntry> entries = new ArrayList<>();

    private SpinnerDemo() {
        // Built-in styles
        for (SpinnerStyle style : SpinnerStyle.values()) {
            entries.add(new SpinnerEntry(
                    style.name(),
                    Spinner.builder().spinnerStyle(style)
                            .style(Style.EMPTY.fg(COLORS[entries.size() % COLORS.length])).build(),
                    new SpinnerState()));
        }
        // Custom frame set
        entries.add(new SpinnerEntry(
                "Custom FrameSet",
                Spinner.builder().frameSet(CUSTOM_FRAMES)
                        .style(Style.EMPTY.fg(COLORS[entries.size() % COLORS.length])).build(),
                new SpinnerState()));
    }

    /**
     * Demo entry point.
     * @param args the CLI arguments
     * @throws Exception on unexpected error
     */
    public static void main(String[] args) throws Exception {
        new SpinnerDemo().run();
    }

    /**
     * Runs the demo application.
     *
     * @throws Exception if an error occurs
     */
    public void run() throws Exception {
        try (Backend backend = BackendFactory.create()) {
            backend.enableRawMode();
            backend.enterAlternateScreen();
            backend.hideCursor();

            Terminal<Backend> terminal = new Terminal<>(backend);

            backend.onResize(() -> terminal.draw(this::ui));

            while (running) {
                terminal.draw(this::ui);

                int c = backend.read(100);
                if (c == 'q' || c == 'Q' || c == 3) {
                    running = false;
                }

                for (SpinnerEntry e : entries) {
                    e.state().advance();
                }
                frameCount++;
            }
        }
    }

    private void ui(Frame frame) {
        Rect area = frame.area();

        var layout = Layout.vertical()
            .constraints(
                Constraint.length(3),  // Header
                Constraint.fill(),     // Main content
                Constraint.length(3)   // Footer
            )
            .split(area);

        renderHeader(frame, layout.get(0));
        renderMainContent(frame, layout.get(1));
        renderFooter(frame, layout.get(2));
    }

    private void renderHeader(Frame frame, Rect area) {
        Block headerBlock = Block.builder()
            .borders(Borders.ALL)
            .borderType(BorderType.ROUNDED)
            .borderStyle(Style.EMPTY.fg(Color.CYAN))
            .title(Title.from(
                Line.from(
                    Span.raw(" TamboUI ").bold().cyan(),
                    Span.raw("Spinner Demo ").yellow()
                )
            ).centered())
            .build();

        frame.renderWidget(headerBlock, area);
    }

    private void renderMainContent(Frame frame, Rect area) {
        int total = entries.size();
        int half = (total + 1) / 2;

        var cols = Layout.horizontal()
            .constraints(
                Constraint.percentage(50),
                Constraint.percentage(50)
            )
            .split(area);

        renderColumn(frame, cols.get(0), 0, half);
        renderColumn(frame, cols.get(1), half, total);
    }

    private void renderColumn(Frame frame, Rect area, int from, int to) {
        int count = to - from;
        Constraint[] constraints = new Constraint[count];
        for (int i = 0; i < count; i++) {
            constraints[i] = Constraint.length(1);
        }
        var rows = Layout.vertical().constraints(constraints).split(area);

        for (int i = 0; i < count; i++) {
            SpinnerEntry e = entries.get(from + i);
            renderSpinnerRow(frame, rows.get(i), e.label(), e.spinner(), e.state());
        }
    }

    private void renderSpinnerRow(Frame frame, Rect area, String label, Spinner spinner, SpinnerState state) {
        var cols = Layout.horizontal()
            .constraints(
                Constraint.length(20),  // Label
                Constraint.length(4),   // Spinner
                Constraint.fill()       // Spacer
            )
            .split(area);

        frame.buffer().setString(cols.get(0).x(), cols.get(0).y(), label, Style.EMPTY.fg(Color.DARK_GRAY));
        frame.renderStatefulWidget(spinner, cols.get(1), state);
    }

    private void renderFooter(Frame frame, Rect area) {
        Line helpLine = Line.from(
            Span.raw(" Frame: ").dim(),
            Span.raw(String.valueOf(frameCount)).bold().cyan(),
            Span.raw("  Spinners: ").dim(),
            Span.raw(String.valueOf(entries.size())).bold().cyan(),
            Span.raw("   "),
            Span.raw("q").bold().yellow(),
            Span.raw(" Quit").dim()
        );

        Paragraph footer = Paragraph.builder()
            .text(Text.from(helpLine))
            .block(Block.builder()
                .borders(Borders.ALL)
                .borderType(BorderType.ROUNDED)
                .borderStyle(Style.EMPTY.fg(Color.DARK_GRAY))
                .build())
            .build();

        frame.renderWidget(footer, area);
    }
}
