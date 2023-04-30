package se.skorup.API.tag_parser;

import java.awt.Color;

/**
 * A text segment in the TerminalOutput
 *
 * @param text the text of the text segment.
 * @param color the color of the text segment.
 * */
public record TextSegment(String text, Color color) {}
