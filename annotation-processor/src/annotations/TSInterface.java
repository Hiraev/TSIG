package annotations;

import javax.lang.model.element.Modifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface TSInterface {
    enum AccessLevel {
        PRIVATE(Modifier.PRIVATE),
        PUBLIC(Modifier.PUBLIC),
        PROTECTED(Modifier.PROTECTED),
        NO(null); // package-private

        private Modifier modifier;

        AccessLevel(Modifier modifier) {
            this.modifier = modifier;
        }

        public Modifier getModifier() {
            return modifier;
        }
    }

    AccessLevel[] accessLevel() default {AccessLevel.PUBLIC};
}