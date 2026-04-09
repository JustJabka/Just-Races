package justjabka.WeltenRaces.Types;

public enum Race {
    ARMAT,
    EPIPHYTE,
    HUMAN,
    LIZARD,
    PHANTOM,
    SKYZERN;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}