/**
 *                                               General
 */

// will move the invisible and visible barrier
var arenaInvisibleWallDistance = 35.0;

var icecreamHealthAmount = 5;

var ambientLight = 0.3;

//All sound will be relative to this max amount
var baseSoundVolume = 100;

// Changes the height (which in turn changes the size of the spot light following the player)
var spotlightHeightAboveAvatar = 10.0;

var gravity = -5.0;

var grenadeBounciness = 0.8;

//rotation controller min/max values used for generating a random float
var rcMin = 0.003;
var rcMax = 0.008;

/**
 * --------------------------------------------------------------------------------------------------
 */

/**
 *                                    GameObject Properties (Location, Scale, etc)
 */

// Avatar initial starting location
var avatarX = 90.0;
var avatarY = 0.6;
var avatarZ = 86.0;

//TerrainPlane
var terrainY = -0.1;
var terrainScale = 50.0;

//Plane
var planeY = 0.0;
var planeScale = 80.0;

//Town location
var townX = 90.0;
var townZ = 90.0;

//Height of orbiter skills
var orbiterYHeight = 0.3;

//location of angel relative to avatar
var angelX = -0.3;
var angelY = 1.0;
var angelZ = -0.3;

// random starting location for grenades
var grenadeX = -100.0;
var grenadeY = 1.0;
var grenadeZ = -100.0;

/**
 * --------------------------------------------------------------------------------------------------
 */

/**
 *                                       Player and Gameplay Specifics
 */

// Player stats
var startingHealth = 50;
var startingLevel = 1;
var startingExperience = 0;
var startingSkillPoint = 1;
var baseSpeed = 1;
var sprintSpeed = .009;
var atk = 10;

// Player Teleport Timings
var teleportDistance = .3;
var teleportCooldownTime = 5.0;

var xpGainedPerOrb = 100;

// The total amount of xp needed to level up
var maxLevelXP = 100;

// Player skills
var fireballLv = 1;
var fireballTravelDistance = 15.0;
var avatarOrbiterLv = 0;
var orbiterSpeed = .03;
var circleLv = 0;
var timeBetweenPassiveHealing = 15.0;
var passiveHealingAmount = 1;
var orbiterLvl1Scale = 2.5;
var orbiterLvl2Scale = 3.5;
var orbiterLvl4Scale = 4.0;

// Player Upgrade Levels (What level the player has to reach before they get a new skill) 
// (Must be in lowest to highest order and no two levels should be the same)
var upgrade1 = 5;
var upgrade2 = 7;
var upgrade3 = 9;
var upgrade4 = 11; 
var upgrade5 = 15;

// Monster stats
var monsterSpeed = 1;
var monsterAtk = 2;
var monsterHealth = 10;

//This value needs to be kept as low as possible. If the avatar takes repeated hits, it will move its physics object out of position
var rangerAttackForce = 1500.0;

/**
 * --------------------------------------------------------------------------------------------------
 */