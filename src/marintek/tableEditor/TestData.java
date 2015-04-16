package marintek.tableEditor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class TestData {

	public class Entry
	{
		private static final String PROP_NUMBER = "number";
		private static final String PROP_NAME = "name";
		PropertyChangeSupport pcs=new PropertyChangeSupport(this);
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			pcs.addPropertyChangeListener(listener);
		}

		public void removePropertyChangeListener(PropertyChangeListener listener) {
			pcs.removePropertyChangeListener(listener);
		}
		/**
		 * @param name
		 * @param number
		 */
		public Entry(String name, int number) {
			this.name = name;
			this.number = number;
		}
		private String name;
		private int number;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			pcs.firePropertyChange(PROP_NAME, this.name, this.name = name);
		}
		public int getNumber() {
			return number;
		}
		public void setNumber(int number) {
			pcs.firePropertyChange(PROP_NUMBER,this.number, this.number = number);
		}
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return String.format("entry %s - %d", name,number);
		}
	}
	private ArrayList<Entry> dataTable;
	
	public void createExample() {
		// TODO Auto-generated method stub
		this.setDataTable(new ArrayList<Entry>());
		getDataTable().add(new Entry("arve", 1));
		getDataTable().add(new Entry("matthias", 2));
		getDataTable().add(new Entry("inge", 3));
		
	}

	public ArrayList<Entry> getDataTable() {
		return dataTable;
	}

	public void setDataTable(ArrayList<Entry> dataTable) {
		this.dataTable = dataTable;
	}

	
}
