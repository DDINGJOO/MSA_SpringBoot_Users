package pk;

import com.github.f4b6a3.ulid.UlidCreator;

public class ULID {
    public String generatedKey()
    {
        return UlidCreator.getUlid().toString();
    }

}
