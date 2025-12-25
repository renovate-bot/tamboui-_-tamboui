///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS dev.tamboui:tamboui-tui:LATEST
//DEPS dev.tamboui:tamboui-jline:LATEST

/*
 * Copyright (c) 2025 TamboUI Contributors
 * SPDX-License-Identifier: MIT
 */
package dev.tamboui.demo;

import dev.tamboui.layout.Constraint;
import dev.tamboui.layout.Layout;
import dev.tamboui.layout.Rect;
import dev.tamboui.terminal.Frame;
import dev.tamboui.text.Text;
import dev.tamboui.tui.Keys;
import dev.tamboui.tui.TuiConfig;
import dev.tamboui.tui.TuiRunner;
import dev.tamboui.tui.event.Event;
import dev.tamboui.tui.event.KeyEvent;
import dev.tamboui.widgets.logo.Logo;
import dev.tamboui.widgets.paragraph.Paragraph;

/**
 * Demo showcasing the Tamboui Logo widget.
 * <p>
 * This demo displays "Powered by" text above the Tamboui logo.
 * The logo size can be controlled via command line arguments:
 * <ul>
 *   <li>No argument: default (tiny) size</li>
 *   <li>"tiny": tiny size</li>
 *   <li>"small": small size (if available)</li>
 * </ul>
 * <p>
 * Press any key to quit.
 */
public class LogoDemo {

    private final Logo.Size size;

    public LogoDemo(Logo.Size size) {
        this.size = size;
    }

    public static void main(String[] args) throws Exception {
        Logo.Size size = Logo.Size.TINY; // Default to tiny
        
        if (args.length > 0) {
            String sizeArg = args[0].toLowerCase();
            switch (sizeArg) {
                case "tiny":
                    size = Logo.Size.TINY;
                    break;
                case "small":
                    // Note: SMALL is not yet implemented in TamboUI Logo
                    // For now, fall back to TINY
                    size = Logo.Size.TINY;
                    break;
                default:
                    size = Logo.Size.TINY;
                    break;
            }
        }

        LogoDemo demo = new LogoDemo(size);
        
        // Use default configuration (fullscreen with alternate screen buffer)
        // Note: Rust version uses Viewport::Inline(3) which is not yet supported in TamboUI
        // See MISSING_FEATURES.md for tracking this feature
        try (TuiRunner tui = TuiRunner.create(TuiConfig.defaults())) {
            tui.run(demo::handleEvent, demo::render);
        }
    }

    private void render(Frame frame) {
        Rect area = frame.area();
        
        // Create vertical layout: 1 line for "Powered by", rest for logo
        Layout layout = Layout.vertical()
            .constraints(
                Constraint.length(1),
                Constraint.fill(1)
            );
        
        var areas = layout.split(area);
        if (areas.size() < 2) {
            return;
        }
        
        Rect topArea = areas.get(0);
        Rect bottomArea = areas.get(1);
        
        // Render "Powered by" text in the top area
        Paragraph poweredBy = Paragraph.builder()
            .text(Text.from("Powered by"))
            .build();
        frame.renderWidget(poweredBy, topArea);
        
        // Render the logo in the bottom area
        Logo logo = Logo.of(size);
        frame.renderWidget(logo, bottomArea);
    }

    private boolean handleEvent(Event event, TuiRunner runner) {
        // Quit on any key press or quit key
        if (Keys.isQuit(event) || event instanceof KeyEvent) {
            runner.quit();
            return false; // Stop the event loop
        }
        return true; // Continue the event loop
    }
}

