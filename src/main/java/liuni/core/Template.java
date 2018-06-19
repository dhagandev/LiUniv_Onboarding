package liuni.core;

import java.util.Optional;
import static java.lang.String.format;

public class Template {
    private final String content;
    private final String defaultMessage;

    public Template(String content, String defaultMessage) {
        this.content = content;
        this.defaultMessage = defaultMessage;
    }

    public String render(Optional<String> message) {
        return format(content, message.orElse(defaultMessage));
    }
}
