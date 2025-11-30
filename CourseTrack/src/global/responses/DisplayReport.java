package global.responses;

import global.LinkedList;
import java.io.Serializable;

public record DisplayReport(LinkedList<String> reportEntries) implements Serializable { }