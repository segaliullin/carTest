public class LineNumberAndValuePair implements Comparable<LineNumberAndValuePair>{
    private Integer lineNumber;
    private String value;

    public LineNumberAndValuePair(Integer lineNumber, String value) {
        this.lineNumber = lineNumber;
        this.value = value;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(LineNumberAndValuePair o) {
        return Integer.compare(this.lineNumber, o.getLineNumber());
    }

    public static LineNumberAndValuePair createKillerObject() {
        return new LineNumberAndValuePair(-1, null);
    }
}
