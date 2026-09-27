package justjabka.justraces.api.common;

import net.kyori.adventure.text.Component;

import java.util.List;

public interface Displayable {
    Component name();
    List<Component> description();
}
