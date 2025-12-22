//DEPS dev.tamboui:tamboui-dsl:LATEST
//JAVA 11+

import dev.tamboui.tui.TuiRunner;
import dev.tamboui.tui.Keys;
import dev.tamboui.widgets.paragraph.Paragraph;
import dev.tamboui.text.Text;

public class HelloWorld {
    public static void main(String[] args) throws Exception {
        try (var tui = TuiRunner.create()) {
            tui.run(
                (event, runner) -> {
                    if (Keys.isQuit(event)) {
                        runner.quit();
                        return false;
                    }
                    return false;
                },
                frame -> {
                    var paragraph = Paragraph.builder()
                        .text(Text.from("Hello, TamboUI! Press 'q' to quit."))
                        .build();
                    frame.renderWidget(paragraph, frame.area());
                }
            );
        }
    }
}