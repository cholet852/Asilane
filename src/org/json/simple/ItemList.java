package org.json.simple;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * |a:b:c| => |a|,|b|,|c| |:| => ||,|| |a:| => |a|,||
 * 
 * @author FangYidong<fangyidong@yahoo.com.cn>
 */
public class ItemList {
	private String sp = ",";
	List items = new ArrayList();

	public ItemList() {
	}

	public ItemList(final String s) {
		this.split(s, sp, items);
	}

	public ItemList(final String s, final String sp) {
		this.sp = s;
		this.split(s, sp, items);
	}

	public ItemList(final String s, final String sp, final boolean isMultiToken) {
		split(s, sp, items, isMultiToken);
	}

	public List getItems() {
		return this.items;
	}

	public String[] getArray() {
		return (String[]) this.items.toArray();
	}

	public void split(final String s, final String sp, final List append, final boolean isMultiToken) {
		if (s == null || sp == null) {
			return;
		}
		if (isMultiToken) {
			final StringTokenizer tokens = new StringTokenizer(s, sp);
			while (tokens.hasMoreTokens()) {
				append.add(tokens.nextToken().trim());
			}
		} else {
			this.split(s, sp, append);
		}
	}

	public void split(final String s, final String sp, final List append) {
		if (s == null || sp == null) {
			return;
		}
		int pos = 0;
		int prevPos = 0;
		do {
			prevPos = pos;
			pos = s.indexOf(sp, pos);
			if (pos == -1) {
				break;
			}
			append.add(s.substring(prevPos, pos).trim());
			pos += sp.length();
		} while (pos != -1);
		append.add(s.substring(prevPos).trim());
	}

	public void setSP(final String sp) {
		this.sp = sp;
	}

	public void add(final int i, final String item) {
		if (item == null) {
			return;
		}
		items.add(i, item.trim());
	}

	public void add(final String item) {
		if (item == null) {
			return;
		}
		items.add(item.trim());
	}

	public void addAll(final ItemList list) {
		items.addAll(list.items);
	}

	public void addAll(final String s) {
		this.split(s, sp, items);
	}

	public void addAll(final String s, final String sp) {
		this.split(s, sp, items);
	}

	public void addAll(final String s, final String sp, final boolean isMultiToken) {
		this.split(s, sp, items, isMultiToken);
	}

	/**
	 * @param i
	 *            0-based
	 * @return
	 */
	public String get(final int i) {
		return (String) items.get(i);
	}

	public int size() {
		return items.size();
	}

	@Override
	public String toString() {
		return toString(sp);
	}

	public String toString(final String sp) {
		final StringBuffer sb = new StringBuffer();

		for (int i = 0; i < items.size(); i++) {
			if (i == 0) {
				sb.append(items.get(i));
			} else {
				sb.append(sp);
				sb.append(items.get(i));
			}
		}
		return sb.toString();

	}

	public void clear() {
		items.clear();
	}

	public void reset() {
		sp = ",";
		items.clear();
	}
}
