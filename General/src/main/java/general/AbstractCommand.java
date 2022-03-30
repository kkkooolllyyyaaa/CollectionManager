package general;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Класс - абстракция каждой из команд
 * содержит абстрактный метод execute
 */
public abstract class AbstractCommand implements Command {
    @Getter
    private final String name;
    @Getter
    private final String description;
    private boolean isStudyGroupCommand = false;

    public AbstractCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public AbstractCommand(String name, String description, boolean isStudyGroupCommand) {
        this(name, description);
        this.isStudyGroupCommand = isStudyGroupCommand;
    }

    public abstract void execute(String[] args);

    @Override
    public boolean isStudyGroupCommand() {
        return isStudyGroupCommand;
    }
}

