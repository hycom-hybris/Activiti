package org.activiti.rest.conf.factory.manager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.rest.conf.util.XmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class HybrisRESTManager {

	private static final String USERGROUP_TAG = "usergroup";
	private static final String CUSTOMER_TAG = "customer";

	public static List<User> getHybrisUsers() {
		Document users = getRESTData(HybrisDataURL.USERS_URL, null);

		if (users == null) {
			return new ArrayList<User>();
		}

		return convertDocumentToUsers(users);
	}

	public static List<Group> getHybrisGroups() {
		Document groups = getRESTData(HybrisDataURL.GROUPS_URL, null);

		if (groups == null) {
			return new ArrayList<Group>();
		}

		return convertDocumentToGroups(groups);
	}

	public static List<User> getHybrisUserById(String uid) {
		Document users = getRESTData(HybrisDataURL.USER_URL, uid);

		if (users == null) {
			return new ArrayList<User>();
		}

		return convertDocumentToUsers(users);

	}

	public static List<Group> getHybrisGroupById(String uid) {
		Document groups = getRESTData(HybrisDataURL.GROUP_URL, uid);

		if (groups == null) {
			return new ArrayList<Group>();
		}

		return convertDocumentToGroups(groups);

	}

	private static enum HybrisDataURL {

		USERS_URL("/rest/customers"), GROUPS_URL(
				"/rest/usergroups"), USER_URL(
						"/rest/customers/"), GROUP_URL(
								"/rest/usergroups/");

		private HybrisDataURL(String url) {
			this.url = url;
		}

		private String url;

		public String getUrl() {
			return url;
		}

	}

	private static Document getRESTData(HybrisDataURL hybrisDataURL, String uid) {

		try {

			String username = "test_admin";
			String password = "nimda";
			String userPassword = username + ":" + password;
			String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());

			URL url = new URL(hybrisDataURL.getUrl() + (StringUtils.isNotBlank(uid) ? uid : ""));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/xml");
			conn.setRequestProperty("Authorization", "Basic " + encoding);

			if (conn.getResponseCode() != 200) {
				// throw new RuntimeException("Failed : HTTP error code : " +
				// conn.getResponseCode() + " url: " + url.toString());
				return null;
			}

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(conn.getInputStream());

			conn.disconnect();

			return doc;

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (SAXException e) {

			e.printStackTrace();

		} catch (ParserConfigurationException e) {

			e.printStackTrace();

		}

		return null;

	}

	private static List<Group> convertDocumentToGroups(Document groups) {

		List<Group> list = new ArrayList<Group>();

		if (groups == null) {
			return list;
		}

		List<Node> nodeList = XmlUtil.asList(groups.getElementsByTagName(USERGROUP_TAG));

		for (Node n : nodeList) {
			list.add(convertElementToUser((Element) n));
		}

		return list;

	}

	private static List<User> convertDocumentToUsers(Document users) {

		List<User> list = new ArrayList<User>();

		if (users == null) {
			return list;
		}

		List<Node> nodeList = XmlUtil.asList(users.getElementsByTagName(CUSTOMER_TAG));

		for (Node n : nodeList) {
			list.add(convertElementToGroup((Element) n));
		}

		return list;

	}

	private static User convertElementToGroup(Element element) {

		String userId = element.getAttribute("uid");

		return new UserEntity(userId);

	}

	private static Group convertElementToUser(Element element) {

		String groupId = element.getAttribute("uid");

		return new GroupEntity(groupId);
	}

}
