import com.ultimatepickaxes.registry.PickaxeDefinition;
import com.ultimatepickaxes.registry.PickaxeJsonLoader;
import com.ultimatepickaxes.registry.VerifyAllPickaxes;

import java.util.List;

public class VerifyAllPickaxesTest {

    public static void main(String[] args) {
        List<PickaxeDefinition> definitions = PickaxeJsonLoader.loadAll();
        VerifyAllPickaxes.verifyAll(definitions);
    }
}
