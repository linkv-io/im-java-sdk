package linkv.io.api.utils;

public enum CheckSumAlgoType {
    MD5("MD5"), SHA_256("SHA-256"), SHA("SHA-512"), SHA_1("SHA1");

    private String name;

    CheckSumAlgoType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName() {
        this.name = name;
    }

}
