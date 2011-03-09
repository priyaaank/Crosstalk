package com.barefoot.crosstalk.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.barefoot.crosstalk.utils.Utils;

public class Criteria<T extends PersistableObject> {
	
	private Map<String, List<String>> whereOptions = new HashMap<String, List<String>>();
	private List<String> fromTables = new ArrayList<String>();
	private List<String> selectFields = new ArrayList<String>();
	private List<Order> orderByFields = new ArrayList<Order>();
	
	public Criteria(T domainObject) {
		fromTables.add(domainObject.getTableName());
	}
	
	public Criteria<T> selectCount() {
		selectFields.add(" count(*) ");
		return this;
	}
	
	public Criteria<T> select(List<String> fields) {
		raiseErrorIfSelectFieldsAlreadySpecified();
		selectFields.addAll(fields);
		return this;
	}
	
	public Criteria<T> select(String...fields) {
		raiseErrorIfSelectFieldsAlreadySpecified();
		selectFields.addAll(Arrays.asList(fields));
		return this;
	}
	
	public Criteria<T> and(String key, String value) {
		where(key,value);
		return this;
	}
	
	public Criteria<T> and(String key, String...value) {
		where(key,value);
		return this;
	}
	
	public Criteria<T> and(String key, List<String> value) {
		where(key, value);
		return this;
	}
		
	public Criteria<T> where(String key, String value) {
		List<String> optionsList = new ArrayList<String>();
		optionsList.add(value);
		whereOptions.put(key, optionsList);
		return this;
	}
	
	public Criteria<T> where(String key, String...value) {
		List<String> optionsList = new ArrayList<String>(Arrays.asList(value));
		whereOptions.put(key, optionsList);
		return this;
	}
	
	public Criteria<T> where(String key, List<String> value) {
		whereOptions.put(key, value);
		return this;
	}
	
	public Criteria<T> orderByAscending(String...columnName) {
		List<String> listOfColumns = Arrays.asList(columnName);
		if(orderByFields.size() == 0) {
			orderByFields.add(new Order(OrderType.ASC, listOfColumns));
			return this;
		}
		
		Order lastInsertedOrder = orderByFields.get(orderByFields.size() - 1);
		if (lastInsertedOrder.type.equals(OrderType.ASC)) {
			lastInsertedOrder.addValues(listOfColumns);
		} else {
			orderByFields.add(new Order(OrderType.ASC, listOfColumns));
		}
		return this;
	}
	
	public Criteria<T> orderByDescending(String...columnName) {
		List<String> listOfColumns = Arrays.asList(columnName);
		if(orderByFields.size() == 0) {
			orderByFields.add(new Order(OrderType.ASC, listOfColumns));
			return this;
		}
		
		Order lastInsertedOrder = orderByFields.get(orderByFields.size() - 1);
		if (lastInsertedOrder.type.equals(OrderType.DESC)) {
			lastInsertedOrder.addValues(listOfColumns);
		} else {
			orderByFields.add(new Order(OrderType.DESC, listOfColumns));
		}
		return this;
	}
	
	public String selectionQuery() {
		StringBuffer query = new StringBuffer(getSelect()).append(" ");
		query.append(getFromClause()).append(" ");
		query.append(getWhereClause()).append(" ");
		query.append(getOrderClause()).append(" ");
		
		return query.toString();
	}
	
	public Map<String, String[]> selectionQueryWithParams() {
		Map<String, String[]> whereOptionsMap = new HashMap<String, String[]>();
		ArrayList<String> values = new ArrayList<String>();
		StringBuffer paramsRepresentationWithQuestionMarks = new StringBuffer("");
        if(whereOptions.size() > 0) {
			StringBuffer whereClause = new StringBuffer("where ");
			for(String fieldName : whereOptions.keySet()) {
				if(whereOptions.get(fieldName).size() > 1) {
					for (String paramVal : whereOptions.get(fieldName)) {
						values.add(paramVal);
						paramsRepresentationWithQuestionMarks.append("?,");
					}
					whereClause.append(fieldName +" in ("+ paramsRepresentationWithQuestionMarks.substring(0, paramsRepresentationWithQuestionMarks.length() - 1) +") and ");
				} else {
					values.add(whereOptions.get(fieldName).get(0));
					whereClause.append(fieldName +" = ? and ");
				}
			}
            whereOptionsMap.put(whereClause.substring(0, whereClause.length() - 5).toString(), values.toArray(new String[values.size()]));
		}

        return whereOptionsMap;
	}
	
	protected String getOrderClause() {
		if(orderByFields.size() > 0) {
			StringBuffer orderClause = new StringBuffer("order by ");
			for (Order eachOrderObject : orderByFields) {
				orderClause.append(eachOrderObject.getOrderByString());
				orderClause.append(", ");
			}
			return orderClause.substring(0, orderClause.length() - 2);
		}
		return "";
	}

	protected String getWhereClause() {
		if(whereOptions.size() > 0) {
			StringBuffer whereClause = new StringBuffer("where ");
			for(String fieldName : whereOptions.keySet()) {
				if(whereOptions.get(fieldName).size() > 1) {
					whereClause.append(fieldName +" in ("+ Utils.getCommaSeparatedStringForAllElements(whereOptions.get(fieldName), true) +") and ");
				} else {
					whereClause.append(fieldName +" = '"+ whereOptions.get(fieldName).get(0)+"' and ");
				}
			}
			return whereClause.substring(0, whereClause.length() - 5);
		}
		return "";
	}

	protected Object getFromClause() {
		StringBuffer fromClause = new StringBuffer();
		fromClause.append("from ");
		int count = 0;
		for(String eachTable : fromTables) {
			fromClause.append(eachTable);
			fromClause.append(" ");
			fromClause.append("t"+count++);
			fromClause.append(", ");
		}
		return fromClause.substring(0, fromClause.length() - 2);
	}

	protected String getSelect() {
		if(selectFields.size() > 0) {
			StringBuffer selectElements = new StringBuffer("select ");
			for(String eachField : selectFields) {
				selectElements.append(eachField);
				selectElements.append(", ");
			}
			return selectElements.substring(0, selectElements.length() - 2);
		}
		return " * ";
	}
	
	private void raiseErrorIfSelectFieldsAlreadySpecified() {
		if (selectFields.size() > 0) {
			throw new InvalidCriteriaConstructionException("Select fields have already been specified");
		}
	}

}

class Order {
	OrderType type;
	List<String> values = new ArrayList<String>();
	
	public Order(OrderType type, List<String> values) {
		this.type = type;
		this.values = values;
	}

	public void addValues(List<String> values) {
		this.values.addAll(values);
	}
	
	public void addValue(String value) {
		values.add(value);
	}
	
	public String getOrderByString() {
		return Utils.getCommaSeparatedStringForAllElements(values, false) + " " + type.getValue();
	}
}

enum OrderType {
	ASC("asc"), 
	DESC("desc");
	
	private String value;
	
	OrderType(String value) {
		this.value = value;
	}
	
	public String getValue() {return this.value;}
}

@SuppressWarnings("serial")
class InvalidCriteriaConstructionException extends RuntimeException {
	
	public InvalidCriteriaConstructionException() {
		super();
	}
	
	public InvalidCriteriaConstructionException(String message) {
		super(message);
	}
	
	public InvalidCriteriaConstructionException(Throwable throwable) {
		super(throwable);
	}
	
	public InvalidCriteriaConstructionException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
