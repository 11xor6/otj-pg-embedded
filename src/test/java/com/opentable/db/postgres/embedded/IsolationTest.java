/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opentable.db.postgres.embedded;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Rule;
import org.junit.Test;

import com.opentable.db.postgres.embedded.EmbeddedPostgreSQLRule;

public class IsolationTest
{
    @Rule
    public EmbeddedPostgreSQLRule pg1 = new EmbeddedPostgreSQLRule();

    @Rule
    public EmbeddedPostgreSQLRule pg2 = new EmbeddedPostgreSQLRule();

    @Test
    public void testIsolation() throws Exception
    {
        Connection c = getConnection(pg1);
        try {
            makeTable(c);
            Connection c2 = getConnection(pg2);
            try {
                makeTable(c2);
            } finally {
                c2.close();
            }
        } finally {
            c.close();
        }
    }

    private void makeTable(Connection c) throws SQLException
    {
        Statement s = c.createStatement();
        s.execute("CREATE TABLE public.foo (a INTEGER)");
    }

    private Connection getConnection(EmbeddedPostgreSQLRule epg) throws SQLException
    {
        return epg.getEmbeddedPostgreSQL().getPostgresDatabase().getConnection();
    }
}
