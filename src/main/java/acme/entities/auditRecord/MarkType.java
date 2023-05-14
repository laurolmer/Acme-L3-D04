
package acme.entities.auditRecord;

public enum MarkType {

	APLUS, A, B, C, F, FMINUS;


	public static MarkType transform(final String mark) {
		MarkType result = null;
		for (final MarkType i : MarkType.values())
			if (i.toString().equals(mark))
				result = i;
		return result;
	}
}
