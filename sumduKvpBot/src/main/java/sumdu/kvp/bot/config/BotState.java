package sumdu.kvp.bot.config;

public enum BotState {
    ENTRY_POINT("/START"),
    HELP_MENU("/HELP"),
    BACK("НАЗАД"),
    DOWNLOAD_PDF("ЗАВАНТАЖИТИ .PDF ФАЙЛ"),
    SOME_TOPIC("");

    private final String text;

    BotState(final String text) {
        this.text = text;
    }

    public String toString() {
        return text;
    }
}