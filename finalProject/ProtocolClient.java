package finalProject;

import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;
import org.joml.*;

import tage.*;
import tage.networking.client.GameConnectionClient;

public class ProtocolClient extends GameConnectionClient {
	private MyGame game;
	private GhostManager ghostManager;
	private UUID id;

	float prevX = 0, prevZ = 0, randX = 0, randZ = 0, trueX = 0, trueZ = 0;

	public ProtocolClient(InetAddress remoteAddr, int remotePort, ProtocolType protocolType, MyGame game)
			throws IOException {
		super(remoteAddr, remotePort, protocolType);
		this.game = game;
		this.id = UUID.randomUUID();
		ghostManager = game.getGhostManager();
	}

	public UUID getID() {
		return id;
	}

	@Override
	protected void processPacket(Object message) {
		String strMessage = (String) message;
		// System.out.println("message received -->" + strMessage);
		String[] messageTokens = strMessage.split(",");

		// Game specific protocol to handle the message
		if (messageTokens.length > 0) {
			// Handle JOIN message
			// Format: (join,success) or (join,failure)
			if (messageTokens[0].compareTo("join") == 0) {
				if (messageTokens[1].compareTo("success") == 0) {
					System.out.println("join success confirmed");
					game.setIsConnected(true);
					sendCreateMessage(game.getPlayerPosition(), (int) game.getPlayerStats().get("class"));
				}
				if (messageTokens[1].compareTo("failure") == 0) {
					System.out.println("join failure confirmed");
					game.setIsConnected(false);
				}
			}

			// Handle BYE message
			// Format: (bye,remoteId)
			if (messageTokens[0].compareTo("bye") == 0) { // remove ghost avatar with id = remoteId
															// Parse out the id into a UUID
				UUID ghostID = UUID.fromString(messageTokens[1]);
				ghostManager.removeGhostAvatar(ghostID);
			}

			// Handle CREATE message
			// Format: (create,remoteId,x,y,z)
			// AND
			// Handle DETAILS_FOR message
			// Format: (dsfr,remoteId,x,y,z)
			if (messageTokens[0].compareTo("create") == 0 || (messageTokens[0].compareTo("dsfr") == 0)) { // create a
				// new ghost avatar Parse out the id into a UUID
				UUID ghostID = UUID.fromString(messageTokens[1]);

				// Parse out the position into a Vector3f
				Vector3f ghostPosition = new Vector3f(
						Float.parseFloat(messageTokens[2]),
						Float.parseFloat(messageTokens[3]),
						Float.parseFloat(messageTokens[4]));

				Integer ghostClass = Integer.parseInt(messageTokens[5]);

				try {
					ghostManager.createGhostAvatar(ghostID, ghostPosition, ghostClass);
				} catch (IOException e) {
					System.out.println("error creating ghost avatar");
				}
			}

			// Handle WANTS_DETAILS message
			// Format: (wsds,remoteId)
			if (messageTokens[0].compareTo("wsds") == 0) {
				// Send the local client's avatar's information
				// Parse out the id into a UUID
				UUID ghostID = UUID.fromString(messageTokens[1]);
				sendDetailsForMessage(ghostID, game.getPlayerPosition(), (int) game.getPlayerStats().get("class"));
			}

			// Handle MOVE message
			// Format: (move,remoteId,x,y,z)
			if (messageTokens[0].compareTo("move") == 0) {
				// move a ghost avatar
				// Parse out the id into a UUID
				UUID ghostID = UUID.fromString(messageTokens[1]);

				// Parse out the position into a Vector3f
				Vector3f ghostPosition = new Vector3f(
						Float.parseFloat(messageTokens[2]),
						Float.parseFloat(messageTokens[3]),
						Float.parseFloat(messageTokens[4]));

				// Parse out the orientation into a Vector3f
				Vector3f ghostOrientation = new Vector3f(
						Float.parseFloat(messageTokens[5]),
						Float.parseFloat(messageTokens[6]),
						Float.parseFloat(messageTokens[7]));

				ghostManager.updateGhostAvatar(ghostID, ghostPosition, ghostOrientation);
			}

			if (messageTokens[0].compareTo("playerStats") == 0) {
				// Parse out the id into a UUID
				UUID ghostID = UUID.fromString(messageTokens[1]);

				HashMap<String, Integer> ghostStats = new HashMap<String, Integer>();
				ghostStats.put("class", Integer.parseInt(messageTokens[2]));
				ghostStats.put("health", Integer.parseInt(messageTokens[3]));
				ghostStats.put("level", Integer.parseInt(messageTokens[4]));
				ghostStats.put("atk", Integer.parseInt(messageTokens[5]));
				ghostStats.put("fireballLv", Integer.parseInt(messageTokens[6]));
				ghostStats.put("avatarOrbiterLv", Integer.parseInt(messageTokens[7]));
				ghostStats.put("circleLv", Integer.parseInt(messageTokens[8]));

				ghostManager.updateGhostAvatarInfo(ghostID, ghostStats);
			}

			if (messageTokens[0].compareTo("changeSkyBoxes") == 0) {
				// Parse out the id into a UUID
				// UUID ghostID = UUID.fromString(messageTokens[1]);

				if (Boolean.parseBoolean(messageTokens[1])) {
					(MyGame.getEngine().getSceneGraph()).setActiveSkyBoxTexture(game.getDaySky());
					MyGame.getEngine().getSceneGraph().setSkyBoxEnabled(true);
				} else {
					(MyGame.getEngine().getSceneGraph()).setActiveSkyBoxTexture(game.getDarkSky());
					MyGame.getEngine().getSceneGraph().setSkyBoxEnabled(true);
				}
			}

			if (messageTokens[0].compareTo("spawnMonster") == 0) {
				prevX = randX;
				prevZ = randZ;
				randX = Float.parseFloat(messageTokens[1]);
				randZ = Float.parseFloat(messageTokens[2]);
				trueX = randX - prevX;
				trueZ = randZ - prevZ;
				// At most 50 enemy on screen
				if (game.monsterNormals.size() < 100) {
					if (!(trueX == 0f && trueZ == 0f)) {
						game.spawnMonsterNormal(randX, 0.6f, randZ);
					}
				} else {
					GameObject deleteGO = game.monsterNormals.get(0);
					(MyGame.getEngine().getSceneGraph()).removeGameObject(deleteGO);
					game.monsterNormals.get(0).setLocalTranslation((new Matrix4f()).translation(50, -20, 50));
					game.monsterNormals.remove(deleteGO);
				}
			}

			// ----------- Ghost NPC Section -------------
			if (messageTokens[0].compareTo("createNPC") == 0) { // create a new ghost NPC
				// Parse out the position
				Vector3f ghostPosition = new Vector3f(
						Float.parseFloat(messageTokens[2]),
						Float.parseFloat(messageTokens[3]),
						Float.parseFloat(messageTokens[4]));
				try {
					ghostManager.createGhostNPC(ghostPosition);
				} catch (IOException e) {
					e.printStackTrace();
				} // error creating ghost avatar
			}

			if (messageTokens[0].compareTo("updateNPC") == 0) { // create a new ghost NPC
				// Parse out the position
				Vector3f ghostPosition = new Vector3f(
						Float.parseFloat(messageTokens[1]),
						Float.parseFloat(messageTokens[2]),
						Float.parseFloat(messageTokens[3]));
				Boolean attackFlag = Boolean.parseBoolean(messageTokens[4]);
				ghostManager.updateGhostNPC(ghostPosition, attackFlag);
			}

			if  (messageTokens[0].compareTo("isnr") == 0) { // create a new ghost NPC
				// Parse out the position
				Vector3f ghostPosition = new Vector3f(
					Float.parseFloat(messageTokens[1]),
					Float.parseFloat(messageTokens[2]),
					Float.parseFloat(messageTokens[3]));
				float criteria = Float.parseFloat(messageTokens[4]);
				float avtarNPCDis = game.getAvatar().getWorldLocation().distance(ghostPosition);
				if (avtarNPCDis <= criteria) {
					sendNearMessage();
				} else {
					sendNotNearMessage();
				}
			}

		}
	}

	// The initial message from the game client requesting to join the
	// server. localId is a unique identifier for the client. Recommend
	// a random UUID.
	// Message Format: (join,localId)

	public void sendJoinMessage() {
		try {
			sendPacket(new String("join," + id.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Informs the server that the client is leaving the server.
	// Message Format: (bye,localId)

	public void sendByeMessage() {
		try {
			sendPacket(new String("bye," + id.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Informs the server of the client�s Avatar�s position. The server
	// takes this message and forwards it to all other clients registered
	// with the server.
	// Message Format: (create,localId,x,y,z) where x, y, and z represent the
	// position

	public void sendCreateMessage(Vector3f position, Integer playerClass) {
		try {
			String message = new String("create," + id.toString());
			message += "," + position.x();
			message += "," + position.y();
			message += "," + position.z();
			message += "," + playerClass;
			sendPacket(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Informs the server of the local avatar's position. The server then
	// forwards this message to the client with the ID value matching remoteId.
	// This message is generated in response to receiving a WANTS_DETAILS message
	// from the server.
	// Message Format: (dsfr,remoteId,localId,x,y,z) where x, y, and z represent the
	// position.

	public void sendDetailsForMessage(UUID remoteId, Vector3f position, Integer playerClass) {
		try {
			String message = new String("dsfr," + remoteId.toString() + "," + id.toString());
			message += "," + position.x();
			message += "," + position.y();
			message += "," + position.z();
			message += "," + playerClass;
			sendPacket(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Informs the server that the local avatar has changed position.
	// Message Format: (move,localId,x,y,z) where x, y, and z represent the
	// position.

	public void sendMoveMessage(Vector3f position, Vector3f orientation) {
		try {
			String message = new String("move," + id.toString());
			message += "," + position.x();
			message += "," + position.y();
			message += "," + position.z();
			message += "," + orientation.x(); // Z
			message += "," + orientation.y(); // Y
			message += "," + orientation.z(); // X
			sendPacket(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendPlayerStatsMessage(HashMap playerStats) {
		try {
			String message = new String("playerStats," + id.toString());
			message += "," + playerStats.get("class");
			message += "," + playerStats.get("health");
			message += "," + playerStats.get("level");
			message += "," + playerStats.get("atk");
			message += "," + playerStats.get("fireballLv");
			message += "," + playerStats.get("avatarOrbiterLv");
			message += "," + playerStats.get("circleLv");
			sendPacket(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendChangeSkyBoxesMessage(boolean switchSkyBoxes) {
		try {
			String message = new String("changeSkyBoxes," + id.toString());
			sendPacket(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendSpawnMonsterMessage() {
		try {
			String message = new String("spawnMonster," + id.toString());
			sendPacket(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendCreateNPCMessage() {
		try {
			String message = new String("needNPC," + id.toString());
			sendPacket(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendNeedNPCInfoMessage() {
		try {
			String message = new String("needNPCInfo," + id.toString());
			sendPacket(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendNearMessage() {
		try {
			String message = new String("isnear," + id.toString());
			sendPacket(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendNotNearMessage() {
		try {
			String message = new String("isNotnear," + id.toString());
			sendPacket(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
