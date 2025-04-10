package application;

public class categores {

	private int cat_id;
	private String categores_name;
	private int number_of_item;

	public categores(int cat_id, String categores_name, int number_of_item) {

		this.cat_id = cat_id;
		this.categores_name = categores_name;
		this.number_of_item = number_of_item;
	}

	public categores(int cat_id, String categores_name) {

		this.cat_id = cat_id;
		this.categores_name = categores_name;
	}

	public int getCat_id() {
		return cat_id;
	}

	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}

	public String getCategores_name() {
		return categores_name;
	}

	public void setCategores_name(String categores_name) {
		this.categores_name = categores_name;
	}

	public int getNumber_of_item() {
		return number_of_item;
	}

	public void setNumber_of_item(int number_of_item) {
		this.number_of_item = number_of_item;
	}

	@Override
	public String toString() {
		return  cat_id +","+ categores_name;
	}

}
