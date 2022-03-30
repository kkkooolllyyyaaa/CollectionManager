package general;

import java.util.Objects;

/**
 * x
 * Класс - абстракция каждой из команд
 * содержит абстрактный метод execute
 */
public abstract class AbstractCommand implements Command {
    private final String name;
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

    /**
     * @return Имя команды
     */
    public final String getName() {
        return name;
    }

    /**
     * @return Описание команды
     */
    public final String getDescription() {
        return description;
    }

    public abstract void execute(String[] args);

    @Override
    public boolean isStudyGroupCommand() {
        return isStudyGroupCommand;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractCommand that = (AbstractCommand) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(name);
    }
}

