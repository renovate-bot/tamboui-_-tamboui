/*
 * Copyright (c) 2025 JRatatui Contributors
 * SPDX-License-Identifier: MIT
 */
package io.github.jratatui.demo;

import io.github.jratatui.backend.jline.JLineBackend;
import io.github.jratatui.layout.Constraint;
import io.github.jratatui.layout.Layout;
import io.github.jratatui.layout.Rect;
import io.github.jratatui.style.Color;
import io.github.jratatui.style.Style;
import io.github.jratatui.terminal.Frame;
import io.github.jratatui.terminal.Terminal;
import io.github.jratatui.text.Line;
import io.github.jratatui.text.Span;
import io.github.jratatui.text.Text;
import io.github.jratatui.widgets.block.Block;
import io.github.jratatui.widgets.block.BorderType;
import io.github.jratatui.widgets.block.Borders;
import io.github.jratatui.widgets.block.Title;
import io.github.jratatui.widgets.paragraph.Paragraph;
import io.github.jratatui.widgets.table.Cell;
import io.github.jratatui.widgets.table.Row;
import io.github.jratatui.widgets.table.Table;
import io.github.jratatui.widgets.table.TableState;
import org.jline.terminal.Terminal.Signal;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;
import java.util.List;

/**
 * Demo TUI application showcasing the Table widget.
 */
public class TableDemo {

    private static final List<String[]> DATA = List.of(
        new String[]{"Alice Johnson", "Software Engineer", "Engineering", "$120,000", "San Francisco"},
        new String[]{"Bob Smith", "Product Manager", "Product", "$135,000", "New York"},
        new String[]{"Charlie Brown", "Data Scientist", "Analytics", "$125,000", "Seattle"},
        new String[]{"Diana Ross", "UX Designer", "Design", "$95,000", "Austin"},
        new String[]{"Edward Chen", "DevOps Engineer", "Engineering", "$115,000", "Portland"},
        new String[]{"Fiona Garcia", "Frontend Developer", "Engineering", "$105,000", "Denver"},
        new String[]{"George Wilson", "Backend Developer", "Engineering", "$110,000", "Chicago"},
        new String[]{"Hannah Lee", "QA Engineer", "Engineering", "$90,000", "Boston"},
        new String[]{"Ivan Petrov", "Security Analyst", "Security", "$130,000", "Washington DC"},
        new String[]{"Julia Martinez", "Technical Writer", "Documentation", "$85,000", "Miami"},
        new String[]{"Kevin O'Brien", "Sales Engineer", "Sales", "$140,000", "Los Angeles"},
        new String[]{"Laura Kim", "HR Manager", "Human Resources", "$100,000", "Atlanta"}
    );

    private boolean running = true;
    private TableState tableState = new TableState();

    public static void main(String[] args) throws Exception {
        new TableDemo().run();
    }

    public void run() throws Exception {
        try (JLineBackend backend = new JLineBackend()) {
            backend.enableRawMode();
            backend.enterAlternateScreen();
            backend.hideCursor();

            Terminal<JLineBackend> terminal = new Terminal<>(backend);

            // Handle resize
            backend.jlineTerminal().handle(Signal.WINCH, signal -> {
                try {
                    terminal.draw(this::ui);
                } catch (IOException e) {
                    // Ignore
                }
            });

            NonBlockingReader reader = backend.jlineTerminal().reader();

            // Select first row
            tableState.selectFirst();

            // Initial draw
            terminal.draw(this::ui);

            // Event loop
            while (running) {
                int c = reader.read(100);
                if (c == -2 || c == -1) {
                    continue;
                }

                boolean needsRedraw = handleInput(c, reader);
                if (needsRedraw) {
                    terminal.draw(this::ui);
                }
            }
        }
    }

    private boolean handleInput(int c, NonBlockingReader reader) throws IOException {
        // Handle escape sequences
        if (c == 27) {
            int next = reader.peek(50);
            if (next == '[') {
                reader.read();
                int code = reader.read();
                return handleEscapeSequence(code);
            }
            return false;
        }

        return switch (c) {
            case 'q', 'Q', 3 -> {
                running = false;
                yield true;
            }
            case 'j', 'J' -> {
                tableState.selectNext(DATA.size());
                yield true;
            }
            case 'k', 'K' -> {
                tableState.selectPrevious();
                yield true;
            }
            case 'g' -> {
                tableState.selectFirst();
                yield true;
            }
            case 'G' -> {
                tableState.selectLast(DATA.size());
                yield true;
            }
            default -> false;
        };
    }

    private boolean handleEscapeSequence(int code) {
        return switch (code) {
            case 'A' -> { // Up
                tableState.selectPrevious();
                yield true;
            }
            case 'B' -> { // Down
                tableState.selectNext(DATA.size());
                yield true;
            }
            default -> false;
        };
    }

    private void ui(Frame frame) {
        Rect area = frame.area();

        List<Rect> layout = Layout.vertical()
            .constraints(
                Constraint.length(3),  // Header
                Constraint.fill(),     // Table
                Constraint.length(5),  // Details
                Constraint.length(3)   // Footer
            )
            .split(area);

        renderHeader(frame, layout.get(0));
        renderTable(frame, layout.get(1));
        renderDetails(frame, layout.get(2));
        renderFooter(frame, layout.get(3));
    }

    private void renderHeader(Frame frame, Rect area) {
        Block headerBlock = Block.builder()
            .borders(Borders.ALL)
            .borderType(BorderType.ROUNDED)
            .borderStyle(Style.EMPTY.fg(Color.CYAN))
            .title(Title.from(
                Line.from(
                    Span.raw(" JRatatui ").bold().cyan(),
                    Span.raw("Table Demo ").yellow()
                )
            ).centered())
            .build();

        frame.renderWidget(headerBlock, area);
    }

    private void renderTable(Frame frame, Rect area) {
        // Create header row
        Row header = Row.from(
            Cell.from("Name").style(Style.EMPTY.bold()),
            Cell.from("Title").style(Style.EMPTY.bold()),
            Cell.from("Department").style(Style.EMPTY.bold()),
            Cell.from("Salary").style(Style.EMPTY.bold()),
            Cell.from("Location").style(Style.EMPTY.bold())
        ).style(Style.EMPTY.fg(Color.YELLOW));

        // Create data rows with alternating colors
        List<Row> rows = new java.util.ArrayList<>();
        for (int i = 0; i < DATA.size(); i++) {
            String[] data = DATA.get(i);
            Style rowStyle = i % 2 == 0 ? Style.EMPTY : Style.EMPTY.bg(Color.indexed(236));
            rows.add(Row.from(data).style(rowStyle));
        }

        Table table = Table.builder()
            .header(header)
            .rows(rows)
            .widths(
                Constraint.percentage(20),  // Name
                Constraint.percentage(20),  // Title
                Constraint.percentage(20),  // Department
                Constraint.length(12),      // Salary
                Constraint.fill()           // Location
            )
            .highlightStyle(Style.EMPTY.bg(Color.BLUE).fg(Color.WHITE).bold())
            .highlightSymbol("▶ ")
            .columnSpacing(1)
            .block(Block.builder()
                .borders(Borders.ALL)
                .borderType(BorderType.ROUNDED)
                .borderStyle(Style.EMPTY.fg(Color.GREEN))
                .title(Title.from(
                    Line.from(
                        Span.raw("Employees "),
                        Span.raw("(" + DATA.size() + " total)").dim()
                    )
                ))
                .build())
            .build();

        frame.renderStatefulWidget(table, area, tableState);
    }

    private void renderDetails(Frame frame, Rect area) {
        Integer selected = tableState.selected();
        Text detailsText;

        if (selected != null && selected < DATA.size()) {
            String[] data = DATA.get(selected);
            detailsText = Text.from(
                Line.from(
                    Span.raw("Name: ").bold(),
                    Span.raw(data[0]).cyan()
                ),
                Line.from(
                    Span.raw("Title: ").bold(),
                    Span.raw(data[1]).green()
                ),
                Line.from(
                    Span.raw("Department: ").bold(),
                    Span.raw(data[2]).yellow(),
                    Span.raw("  |  Salary: ").bold(),
                    Span.raw(data[3]).magenta(),
                    Span.raw("  |  Location: ").bold(),
                    Span.raw(data[4]).blue()
                )
            );
        } else {
            detailsText = Text.from(Line.from(Span.raw("No employee selected").dim()));
        }

        Paragraph details = Paragraph.builder()
            .text(detailsText)
            .block(Block.builder()
                .borders(Borders.ALL)
                .borderType(BorderType.ROUNDED)
                .borderStyle(Style.EMPTY.fg(Color.MAGENTA))
                .title("Details")
                .build())
            .build();

        frame.renderWidget(details, area);
    }

    private void renderFooter(Frame frame, Rect area) {
        Line helpLine = Line.from(
            Span.raw(" j/↓").bold().yellow(),
            Span.raw(" Down  ").dim(),
            Span.raw("k/↑").bold().yellow(),
            Span.raw(" Up  ").dim(),
            Span.raw("g").bold().yellow(),
            Span.raw(" First  ").dim(),
            Span.raw("G").bold().yellow(),
            Span.raw(" Last  ").dim(),
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
