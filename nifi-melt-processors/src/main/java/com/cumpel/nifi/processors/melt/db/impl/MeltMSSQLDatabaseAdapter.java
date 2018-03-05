/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cumpel.nifi.processors.melt.db.impl;

import org.apache.commons.lang3.StringUtils;
import com.cumpel.nifi.processors.melt.db.MeltDatabaseAdapter;
import org.apache.nifi.processors.standard.db.impl.MSSQLDatabaseAdapter;

/**
 * A database adapter that generates MS SQL Compatible SQL.
 */
public class MeltMSSQLDatabaseAdapter extends MSSQLDatabaseAdapter implements MeltDatabaseAdapter  {
    @Override
    public String getName() {
        return "MS SQL 2012+";
    }

    @Override
    public String getDescription() {
        return "Generates MS SQL Compatible SQL, for version 2012 or greater";
    }

    @Override
    public String getSelectStatement(String tableName, String columnNames, String whereClause, String orderByClause, Long limit, Long offset) {
        if (StringUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("Table name cannot be null or empty");
        }
        final StringBuilder query = new StringBuilder("SELECT ");

        //If this is a limit query and not a paging query then use TOP in MS SQL
        if (limit != null && offset == null){
            query.append("TOP ");
            query.append(limit);
            query.append(" ");
        }

        if (StringUtils.isEmpty(columnNames) || columnNames.trim().equals("*")) {
            query.append("*");
        } else {
            query.append(columnNames);
        }
        query.append(" FROM ");
        query.append(tableName);

        if (!StringUtils.isEmpty(whereClause)) {
            query.append(" WHERE ");
            query.append(whereClause);
        }
        if (!StringUtils.isEmpty(orderByClause)) {
            query.append(" ORDER BY ");
            query.append(orderByClause);
        }
        if (offset != null && limit != null && limit > 0) {
            if (StringUtils.isEmpty(orderByClause)) {
                throw new IllegalArgumentException("Order by clause cannot be null or empty when using row paging");
            }

            query.append(" OFFSET ");
            query.append(offset);
            query.append(" ROWS");

            query.append(" FETCH NEXT ");
            query.append(limit);
            query.append(" ROWS ONLY");
        }

        return query.toString();
    }
}
