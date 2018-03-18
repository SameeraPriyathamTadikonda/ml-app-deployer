package com.marklogic.mgmt.resource.restapis;

import com.fasterxml.jackson.databind.JsonNode;
import com.marklogic.client.ext.helper.LoggingObject;
import com.marklogic.mgmt.ManageClient;
import com.marklogic.mgmt.ManageResponse;
import com.marklogic.mgmt.PayloadParser;
import com.marklogic.mgmt.resource.appservers.ServerManager;
import com.marklogic.mgmt.resource.databases.DatabaseManager;
import org.springframework.http.HttpMethod;

/**
 * For /v1/rest-apis. Currently only supports JSON files.
 */
public class RestApiManager extends LoggingObject {

	private PayloadParser payloadParser = new PayloadParser();
	private ManageClient client;

	public RestApiManager(ManageClient client) {
		this.client = client;
	}

	public ManageResponse createRestApi(String json) {
		return createRestApi(extractNameFromJson(json), json);
	}

	public ManageResponse createRestApi(String name, String json) {
		logger.info("Checking for existence of REST API with name: " + name);
		if (restApiServerExists(name)) {
			logger.info("REST API server already exists with name: " + name);
			return null;
		} else {
			logger.info("Creating REST API: " + json);
			ManageResponse re = client.postJson("/v1/rest-apis", json);
			logger.info("Created REST API");
			return re;
		}
	}

	public String extractNameFromJson(String json) {
		JsonNode node = payloadParser.parseJson(json);
		return node.get("rest-api").get("name").textValue();
	}

	/**
	 * Prior to ML 9.0-4, the /v1/rest-apis endpoint required that a server's url-rewriter have the string "rest-api"
	 * somewhere in it. With 9.0-4, the url-rewriter must match the pattern:
	 * <p>
	 * ^/MarkLogic/rest-api/(8000-rewriter|rewriter|rewriter-noxdbc)\.xml$
	 * <p>
	 * It's not likely that a user's custom rewriter will fit that pattern, so this method no longer uses /v1/rest-apis,
	 * opting to use ServerManager instead.
	 *
	 * @param name
	 * @return
	 */
	public boolean restApiServerExists(String name) {
		return new ServerManager(client).exists(name);
	}

	/**
	 * Will need to wait for MarkLogic to restart, so consider using AdminManager with this.
	 *
	 * @param request
	 * @return
	 */
	public boolean deleteRestApi(RestApiDeletionRequest request) {
		ServerManager serverManager = new ServerManager(client, request.getGroupName());
		final String serverName = request.getServerName();
		if (serverManager.exists(serverName)) {
			String path = format("/v1/rest-apis/%s", serverName);

			if (request.isIncludeModules() || request.isIncludeContent()) {
				path += "?";

				DatabaseManager databaseManager = new DatabaseManager(client);
				String payload = serverManager.getPropertiesAsJson(serverName);
				PayloadParser parser = new PayloadParser();

				if (request.isIncludeModules()) {
					if (request.isDeleteModulesReplicaForests()) {
						String modulesDatabase = parser.getPayloadFieldValue(payload, "modules-database");
						if (databaseManager.exists(modulesDatabase)) {
							databaseManager.deleteReplicaForests(modulesDatabase);
						}
					}
					path += "include=modules&";
				}

				if (request.isIncludeContent()) {
					if (request.isDeleteContentReplicaForests()) {
						String contentDatabase = parser.getPayloadFieldValue(payload, "content-database");
						if (databaseManager.exists(contentDatabase)) {
							databaseManager.deleteReplicaForests(contentDatabase);
						}
					}
					path += "include=content";
				}
			}

			logger.info("Deleting REST API, path: " + path);
			client.delete(path);
			logger.info("Deleted REST API");
			return true;
		} else {
			logger.info(format("Server %s does not exist, not deleting", serverName));
			return false;
		}
	}
}
