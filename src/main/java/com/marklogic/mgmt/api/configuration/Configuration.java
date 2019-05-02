package com.marklogic.mgmt.api.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.marklogic.mgmt.util.ObjectMapperFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Does not yet support XML marshalling via JAXB.
 * <p>
 * Using ObjectNode for the lists instead of subclasses of Resource so that clients are not forced to unmarshal JSON
 * into Resource objects that may not fully map everything to the corresponding Manage schema.
 */
public class Configuration {

	@JsonProperty("amp")
	private List<ObjectNode> amps;

	@JsonProperty("database")
	private List<ObjectNode> databases;

	@JsonProperty("forest")
	private List<ObjectNode> forests;

	@JsonProperty("group")
	private List<ObjectNode> groups;

	@JsonProperty("privilege")
	private List<ObjectNode> privileges;

	@JsonProperty("role")
	private List<ObjectNode> roles;

	@JsonProperty("server")
	private List<ObjectNode> servers;

	@JsonProperty("user")
	private List<ObjectNode> users;

	public boolean hasResources() {
		return
			(amps != null && !amps.isEmpty()) ||
				(databases != null && !databases.isEmpty()) ||
				(forests != null && !forests.isEmpty()) ||
				(groups != null && !groups.isEmpty()) ||
				(privileges != null && !privileges.isEmpty()) ||
				(roles != null && !roles.isEmpty()) ||
				(servers != null && !servers.isEmpty()) ||
				(users != null && !users.isEmpty());

	}

	public void addAmp(ObjectNode amp) {
		if (amps == null) amps = new ArrayList<>();
		amps.add(amp);
	}

	protected ObjectNode readJson(String json) {
		try {
			return (ObjectNode) ObjectMapperFactory.getObjectMapper().readTree(json);
		} catch (IOException e) {
			throw new RuntimeException("Unable to read JSON into an ObjectNode, cause: " + e.getMessage(), e);
		}
	}

	public void addDatabase(ObjectNode d) {
		if (databases == null) databases = new ArrayList<>();
		databases.add(d);
	}

	public void addForest(ObjectNode f) {
		if (forests == null) forests = new ArrayList<>();
		forests.add(f);
	}

	public void addGroup(ObjectNode g) {
		if (groups == null) groups = new ArrayList<>();
		groups.add(g);
	}

	public void addRole(ObjectNode r) {
		if (roles == null) roles = new ArrayList<>();
		roles.add(r);
	}

	public void addServer(ObjectNode s) {
		if (servers == null) servers = new ArrayList<>();
		servers.add(s);
	}

	public void addUser(ObjectNode u) {
		if (users == null) users = new ArrayList<>();
		users.add(u);
	}

	public void addPrivilege(ObjectNode p) {
		if (privileges == null) privileges = new ArrayList<>();
		privileges.add(p);
	}

	public List<ObjectNode> getAmps() {
		return amps;
	}

	public void setAmps(List<ObjectNode> amps) {
		this.amps = amps;
	}

	public List<ObjectNode> getForests() {
		return forests;
	}

	public void setForests(List<ObjectNode> forests) {
		this.forests = forests;
	}

	public List<ObjectNode> getDatabases() {
		return databases;
	}

	public void setDatabases(List<ObjectNode> databases) {
		this.databases = databases;
	}

	public List<ObjectNode> getGroups() {
		return groups;
	}

	public void setGroups(List<ObjectNode> groups) {
		this.groups = groups;
	}

	public List<ObjectNode> getRoles() {
		return roles;
	}

	public void setRoles(List<ObjectNode> roles) {
		this.roles = roles;
	}

	public List<ObjectNode> getServers() {
		return servers;
	}

	public void setServers(List<ObjectNode> servers) {
		this.servers = servers;
	}

	public List<ObjectNode> getUsers() {
		return users;
	}

	public void setUsers(List<ObjectNode> users) {
		this.users = users;
	}

	public List<ObjectNode> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<ObjectNode> privileges) {
		this.privileges = privileges;
	}
}
