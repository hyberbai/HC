package hylib.data;

import java.util.EnumSet;

public enum DataRowState {
	Unchanged,
	Detached,
	Added,
	Deleted,
	Modified;

    public static final EnumSet<DataRowState> Changed = EnumSet.of(Detached, Added, Deleted, Modified);
}
