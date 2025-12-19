# JRatatui

A pure Java port of [ratatui](https://github.com/ratatui/ratatui), the Rust library for building terminal user interfaces.
Most of the code was automatically ported using AI.

## Overview

JRatatui brings the immediate-mode TUI paradigm from Rust to the Java ecosystem. It provides a set of widgets and a layout system for building rich terminal applications with modern Java idioms.

### Key Features

- **Immediate-mode rendering** - Redraw the entire UI each frame for simple state management
- **Intermediate buffer system** - Widgets render to a buffer, enabling diff-based terminal updates
- **Constraint-based layout** - Flexible layout system with percentage, fixed, and proportional sizing
- **Modern Java idioms** - Records, sealed interfaces, pattern matching (Java 21+)
- **JLine 3 backend** - Cross-platform terminal support including Windows via Jansi
- **GraalVM native image support** - Compile to native executables for instant startup

## Modules

| Module | Description |
|--------|-------------|
| `jratatui-core` | Core types: Buffer, Cell, Rect, Style, Layout, Text primitives |
| `jratatui-widgets` | Widget implementations: Block, Paragraph, List, TextInput, etc. |
| `jratatui-jline` | JLine 3 terminal backend |
| `demos/*` | Demo applications showcasing widgets and features |

## Requirements

- Java 21 or later
- Gradle 9.2.1 or later

## Quick Start

```java
import io.github.jratatui.backend.jline.JLineBackend;
import io.github.jratatui.terminal.Terminal;
import io.github.jratatui.widgets.block.Block;
import io.github.jratatui.widgets.paragraph.Paragraph;
import io.github.jratatui.text.Text;

public class HelloWorld {
    public static void main(String[] args) throws Exception {
        try (var backend = new JLineBackend();
             var terminal = new Terminal<>(backend)) {

            terminal.draw(frame -> {
                var paragraph = Paragraph.builder()
                    .text(Text.from("Hello, JRatatui!"))
                    .block(Block.bordered())
                    .build();

                frame.renderWidget(paragraph, frame.area());
            });

            // Wait for keypress
            System.in.read();
        }
    }
}
```

## Building

```bash
# Build the project
./gradlew build

# Run the basic demo
./gradlew :demos:basic-demo:run

# Build native image (requires GraalVM)
./gradlew :demos:basic-demo:nativeCompile
```

## Widgets

### Implemented

- **Block** - Container with borders and titles
- **Paragraph** - Multi-line text with wrapping and alignment
- **List** - Scrollable list with selection
- **TextInput** - Single-line text input field

### Planned

- Table, Tabs, Gauge, LineGauge, Sparkline, BarChart, Chart, Canvas, Scrollbar, Calendar

## Acknowledgments

This project is a port of [ratatui](https://github.com/ratatui/ratatui), an excellent Rust TUI library. We thank the ratatui maintainers and contributors for their work.

## License

MIT License - see [LICENSE](LICENSE) for details.

This project is licensed under the same terms as the original ratatui project to ensure compatibility.
