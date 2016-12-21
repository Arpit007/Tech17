package com.nitkkr.gawds.tech16.api1;

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
	public enum QueryTargetType
	{
		Informals,
		GuestTalk,
		Exhibition
	}

	private String query;
	private QueryType queryType;
	private QueryTargetType targetType;

	public Query(String query, QueryType queryType, QueryTargetType queryTargetType)
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
	public QueryTargetType getQueryTargetType(){return targetType;}

	public void setQueryType(QueryType queryType)
	{
		this.queryType = queryType;
	}
	public void setQuery(String query)
	{
		this.query = query;
	}
	public void setQueryTargetType(QueryTargetType targetType){this.targetType=targetType;}
}
