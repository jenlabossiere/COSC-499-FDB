package Info;

public final class Allergy {
    private final int ID;
    private final String NAME;
    
    private Allergy(int id, String name) {
        ID = id;
        NAME = name;
    }

    public static final Allergy createFdbAllergy(int damAllergenCode, String damAllergenName) {
        return new Allergy(damAllergenCode, damAllergenName);
    }

    public final int getId() {
        return ID;
    }

    public final String getAllergyName() {
        return NAME;
    }
}