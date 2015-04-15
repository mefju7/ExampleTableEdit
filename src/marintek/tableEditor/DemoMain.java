/**
 * 
 */
package marintek.tableEditor;


import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;

/**
 * @author Matthias
 *
 */
public class DemoMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("program started 2015-04-15/1");
		final Display display = new Display();
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			public void run() {
				try {
					TestWindow tw = new TestWindow(display);
					tw.open();
					tw.layout();
					// message loop
					while (display.getShells().length > 0) {
						if (!display.readAndDispatch()) {
							display.sleep();
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
			}
		});
		display.dispose();
		System.out.println("program ended 2015-04-15/2");
	}

}
