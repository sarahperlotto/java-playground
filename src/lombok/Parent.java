package lombok;

import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Parent {
    private int id;
    private String name;
}
