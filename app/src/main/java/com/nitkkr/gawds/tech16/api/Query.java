package com.nitkkr.gawds.tech16.api;

import java.io.Serializable;

/**
 * Created by Home Laptop on 03-Dec-16.
 */

public class Query implements Serializable
{

	public enum QueryType
	{
		SQl,
		Web
	}

	private String query;
	private QueryType queryType;
	private EventTargetType targetType;

	public Query(String query, QueryType queryType, EventTargetType queryTargetType)
	{
		this.query=query;
		this.queryType=queryType;
		this.targetType=queryTargetType;
	}

	public QueryType getQueryType()
	{
		return queryType;
	}
	public String getQuery()
	{
		return query;
	}
	public EventTargetType getQueryTargetType(){return targetType;}

	public void setQueryType(QueryType queryType)
	{
		this.queryType = queryType;
	}
	public void setQuery(String query)
	{
		this.query = query;
	}
	public void setQueryTargetType(EventTargetType targetType){this.targetType=targetType;}
}
