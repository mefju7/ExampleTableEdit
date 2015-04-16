package marintek.tableEditor;

import java.util.ArrayList;

import marintek.tableEditor.TestData.Entry;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class TestWindow extends Shell {

	private static final String COL_B = "ColB";
	private static final String COL_A = "ColA";

	private TestData testData;
	// private Table table;
	private TableViewer tableViewer;
	private WritableList wl;
	private Table table;

	public TestWindow(Display display) {
		setSize(620, 479);
		// TODO Auto-generated constructor stub
		this.testData = new TestData();
		testData.createExample();
		setLayout(new GridLayout(1, false));

		Menu menu = new Menu(this, SWT.BAR);
		setMenuBar(menu);
		createContent(this);

		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");

		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);

		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		mntmExit.setText("Exit");

		MenuItem mntmRefresh = new MenuItem(menu, SWT.NONE);
		mntmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableViewer.refresh();
			}
		});
		mntmRefresh.setText("Refresh");

		//
		//
		tableViewer.setContentProvider(new ObservableListContentProvider());
		//
		wl = new WritableList(testData.getDataTable(), TestData.Entry.class);
		tableViewer.setInput(wl);

		MenuItem mntmNewRow = new MenuItem(menu, SWT.NONE);
		mntmNewRow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Entry rowAdded = testData.addRow();
				wl.add(rowAdded); // adding to writeable list updates table and
									// data model
				tableViewer.editElement(rowAdded, 1); // starts editing
			}
		});
		mntmNewRow.setText("New Row");

		MenuItem mntmDebug = new MenuItem(menu, SWT.NONE);
		mntmDebug.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ArrayList<Entry> dt = testData.getDataTable();
				for (Entry et : dt) {
					System.out.println(et.toString());
				}
				System.out.printf("have %d table items, %d wl items\n",table.getItemCount(),wl.size());
			}
		});
		mntmDebug.setText("Debug");
	}

	private void createContent(Composite parent) {
		// TODO Auto-generated method stub
		tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		 table = tableViewer.getTable();
		TableCursor cursor = new TableCursor(table, SWT.FULL_SELECTION);

		TextCellEditor te = new TextCellEditor(table);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TableViewerColumn tvColA = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnA = tvColA.getColumn();
		tblclmnA.setWidth(100);
		tblclmnA.setText(COL_A);
		tblclmnA.setAlignment(SWT.RIGHT);
		tvColA.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				Object element = cell.getElement();
				if (element instanceof Entry) {
					Entry entry = (Entry) element;
					cell.setText(entry.getName());
				}
			}
		});
		tvColA.setEditingSupport(new EditingSupport(tableViewer) {

			@Override
			protected void setValue(Object element, Object value) {
				Entry e = (Entry) element;
				String str = (String) value;
				e.setName(str);
				tableViewer.refresh(e, true);
				cursor.setSelection(wl.indexOf(e), 0);

			}

			@Override
			protected Object getValue(Object element) {
				Entry e = (Entry) element;
				return e.getName();
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				// TODO Auto-generated method stub
				return te;
			}

			@Override
			protected boolean canEdit(Object element) {
				if (!(element instanceof Entry)) {
					System.out.println("additional element?");
					return false;
				}
				return true;
			}
		});

		TableViewerColumn tvColB = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnB = tvColB.getColumn();
		tblclmnB.setWidth(100);
		tblclmnB.setText(COL_B);
		tblclmnB.setAlignment(SWT.CENTER);
		Color bgColor = new Color(parent.getDisplay(), 255, 128, 128);
		tvColB.setLabelProvider(new StyledCellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				Object element = cell.getElement();
				if (element instanceof Entry) {
					Entry entry = (Entry) element;
					String num = String.format("%04d", entry.getNumber());
					cell.setText(num);
				}

			}

			@Override
			protected void paint(Event event, Object element) {
				if(!(element instanceof Entry))
					return;
				int wa = tblclmnB.getWidth();
				int x = event.x;
				int y = event.y;
				int h = event.height - 1;
				Entry e = (Entry) element;
				int w = (e.getNumber() * wa) / 50;
				GC gc = event.gc;
				Color oldColor = gc.getBackground();
				gc.setBackground(bgColor);
				gc.fillRectangle(x, y, w, h);
				gc.setBackground(oldColor);
				// next one prints number
				super.paint(event, element);
			}
		});

		tvColB.setEditingSupport(new EditingSupport(tableViewer) {

			@Override
			protected void setValue(Object element, Object value) {
				Entry e = (Entry) element;
				String str = (String) value;
				try {
					e.setNumber(Integer.parseInt(str));
				} catch (Exception ex) {
				}
				tableViewer.refresh(e, true);
			}

			@Override
			protected Object getValue(Object element) {

				Entry e = (Entry) element;
				return String.format("%d", e.getNumber());
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				// TODO Auto-generated method stub
				return te;
			}

			@Override
			protected boolean canEdit(Object element) {
				// TODO Auto-generated method stub
				return (element instanceof Entry);
			}
		});
		// adds a cursor to the table

		cursor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				Object element = cursor.getRow().getData();
				Entry data2;
				try
				{
				data2 = (Entry) element;
				}
				catch(ClassCastException ex)
				{
					data2=testData.emptyRow();
					wl.add(data2);
					tableViewer.refresh();
				}
				tableViewer.editElement(data2, cursor.getColumn());
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] sel = table.getSelection();
				if ((sel.length != 1) || (sel[0] != cursor.getRow())) {
					System.out.println("setting table selection");
					table.setSelection(cursor.getRow());
				}
			}
		});

		table.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
				if (cursor.getRow() == null) {
					System.out.println("cursor not visible");
					cursor.setSelection(table.getSelectionIndex(), 0);
					// cursor.setVisible(true);
					cursor.setFocus();
				}
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
//				widgetSelected(e);

			}
		});
		cursor.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.INSERT) {
					Entry er = testData.emptyRow();
					wl.add(table.getSelectionIndex(), er);
					tableViewer.refresh(er, true);
					tableViewer.editElement(er, 0);
				}
				if (e.keyCode == SWT.DEL) {
					wl.remove(table.getSelectionIndex());
				}				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});
		//

		//
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
