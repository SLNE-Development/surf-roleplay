package dev.slne.surf.roleplay.paper

//val plugin get() = JavaPlugin.getPlugin(SurfRoleplay::class.java)
//
//class SurfRoleplay : SuspendingJavaPlugin() {
//
//    private lateinit var rpDatabase: RpDatabase
//
//    override suspend fun onLoadAsync() {
//        RpCore.plugin = this
//        rpDatabase = RpDatabase(dataPath)
//
//        mechanicRegistryImpl.registerMechanics(this)
//        mechanicRegistryImpl.registerDatabaseTables(rpDatabase)
//
//        licenseServiceImpl.onLoad()
//
//        rpDatabase.onLoad()
//
//        mechanicRegistryImpl.loadMechanics()
//    }
//
//    override suspend fun onEnableAsync() {
//        ListenerManager.registerListeners()
//
//        mechanicRegistryImpl.enableMechanics()
//        mechanicRegistryImpl.registerBukkitHandlers()
//
//        licenseServiceImpl.onEnable()
//
//
//    }
//
//    override suspend fun onDisableAsync() {
//        licenseServiceImpl.onDisable()
//        mechanicRegistryImpl.disableMechanics()
//
//        rpDatabase.onDisable()
//    }
//
//}