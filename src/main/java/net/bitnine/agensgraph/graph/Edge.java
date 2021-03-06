/*
 * Copyright (c) 2014-2017, Bitnine Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.bitnine.agensgraph.graph;

import net.bitnine.agensgraph.util.Jsonb;
import org.json.simple.JSONObject;
import org.postgresql.util.PGobject;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Edge extends PGobject implements Serializable, Cloneable {
    private static final Pattern edgePattern;

    private String label;
    private GraphId eid;
    private GraphId start;
    private GraphId end;
    private Jsonb props;

    static {
        edgePattern = Pattern.compile("(.+?)\\[(.+?)\\]\\[(.+?),(.+?)\\](.*)");
    }

    public Edge() {
        setType("edge");
    }

    @Override
    public void setValue(String value) throws SQLException {
        Matcher m = edgePattern.matcher(value);
        if (m.find()) {
            this.label = m.group(1);

            GraphId gid = new GraphId();
            gid.setValue(m.group(2));
            this.eid = gid;

            gid = new GraphId();
            gid.setValue(m.group(3));
            this.start = gid;

            gid = new GraphId();
            gid.setValue(m.group(4));
            this.end = gid;

            Jsonb props = new Jsonb();
            props.setValue(m.group(5));
            if (!(props.getJsonValue() instanceof JSONObject))
                throw new PSQLException("Parsing edge failed", PSQLState.DATA_ERROR);
            this.props = props;
        } else {
            throw new PSQLException("Parsing edge failed", PSQLState.DATA_ERROR);
        }

        super.setValue(value);
    }

    public String getLabel() {
        return label;
    }

    public GraphId getEdgeId() {
        return eid;
    }

    public GraphId getStartVertexId() {
        return start;
    }

    public GraphId getEndVertexId() {
        return end;
    }

    public Jsonb getProperties() {
        return props;
    }

    public String getString(String key) {
        return props.getString(key);
    }

    public int getInt(String key) {
        return props.getInt(key);
    }

    public long getLong(String key) {
        return props.getLong(key);
    }

    public double getDouble(String key) {
        return props.getDouble(key);
    }

    public boolean getBoolean(String key) {
        return props.getBoolean(key);
    }

    public boolean isNull(String key) {
        return props.isNull(key);
    }

    public Jsonb getArray(String key) {
        return props.getArray(key);
    }

    public Jsonb getObject(String key) {
        return props.getObject(key);
    }
}
