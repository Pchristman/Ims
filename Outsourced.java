package C482PA.Model;

public class Outsourced extends Part {

    private String companyName;

    /**
     * Constructor for Outsourced
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param companyName
     */
    public Outsourced (int id, String name, double price, int stock, int min, int max, String companyName ) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Sets the String companyName for the object
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Returns the company name
     * @return
     */
    public String getCompanyName() {
        return companyName;
    }
}
