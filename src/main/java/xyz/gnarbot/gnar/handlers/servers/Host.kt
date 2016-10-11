package xyz.gnarbot.gnar.handlers.servers

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import net.dv8tion.jda.JDA
import net.dv8tion.jda.entities.Guild
import net.dv8tion.jda.entities.User
import net.dv8tion.jda.events.message.MessageReceivedEvent
import net.dv8tion.jda.exceptions.PermissionException
import net.dv8tion.jda.managers.GuildManager
import org.json.JSONObject
import xyz.gnarbot.gnar.Bot
import xyz.gnarbot.gnar.handlers.commands.CommandHandler
import xyz.gnarbot.gnar.handlers.members.MemberHandler
import xyz.gnarbot.gnar.utils.NullableJSON
import xyz.gnarbot.gnar.utils.child
import java.io.File

/**
 * Represents a bot on each [Guild].
 */
class Host(val shard : Shard, guild : Guild) : GuildManager(guild), Guild by guild
{
    lateinit var file : File
        private set
    lateinit var jsonObject : JSONObject
        private set
    
    val memberHandler = MemberHandler(this)
    val commandHandler = CommandHandler(this)
    
    /** Dependency injection instance from Guice. */
    private val injector : Injector = Guice.createInjector(HostModule())
    
    init
    {
        loadJSON()
        saveJSON()
        
        commandHandler.recieveFrom(shard.distributor)
        commandHandler.registry.values.forEach { injector.injectMembers(it) }
    }
    
    /** Hanldles incoming message events.*/
    fun handleMessageEvent(event : MessageReceivedEvent)
    {
        if (event.isPrivate) return
        commandHandler.callCommand(event)
    }
    
    /** Load JSON instance from the Host's storage. */
    fun loadJSON()
    {
        file = Bot.files.hosts.child("$id.json")
        file.createNewFile()
    
        val content = file.readText()
        if (content.length == 0) jsonObject = NullableJSON()
        else jsonObject = NullableJSON(content)
    }
    
    /** Save the JSON instance of the Host. */
    fun saveJSON() = file.writeText(jsonObject.toString(4))
    
    /**
     * @return String representation of the Host.
     */
    override fun toString() : String = "Host(id=$id, shard=${shard.id}, guild=\"${guild.name}\")"
    
    /**
     * Attempt to ban the member from the guild.
     * @return If the bot had permission.
     */
    fun banUser(user : User) : Boolean
    {
        try
        {
            ban(user, 0)
            return true
        }
        catch (e : PermissionException)
        {
            return false
        }
    }
    
    /**
     * Attempt to unBan the member from the guild.
     * @return If the bot had permission.
     */
    fun unbanUser(user : User) : Boolean
    {
        try
        {
            unBan(user)
            return true
        }
        catch (e : PermissionException)
        {
            return false
        }
    }
    
    /**
     * Attempt to kick the member from the guild.
     * @return If the bot had permission.
     */
    fun kickUser(user : User) : Boolean
    {
        try
        {
            kick(user)
            return true
        }
        catch (e : PermissionException)
        {
            return false
        }
    }
    
    /**
     * Attempt to mute the member from the guild.
     * @return If the bot had permission.
     */
    fun muteUser(user : User) : Boolean
    {
        try
        {
            mute(user)
            return true
        }
        catch (e : PermissionException)
        {
            return false
        }
    }
    
    /**
     * Attempt to unmute the member from the guild.
     * @return If the bot had permission.
     */
    fun unmuteUser(user : User) : Boolean
    {
        try
        {
            unmute(user)
            return true
        }
        catch (e : PermissionException)
        {
            return false
        }
    }
    
    /**
     * Injector module for the Host.
     */
    private inner class HostModule : AbstractModule()
    {
        public override fun configure()
        {
            bind(Guild::class.java).toInstance(guild)
            bind(CommandHandler::class.java).toInstance(commandHandler)
            bind(MemberHandler::class.java).toInstance(memberHandler)
            bind(Host::class.java).toInstance(this@Host)
            bind(Shard::class.java).toInstance(shard)
            bind(JDA::class.java).toInstance(jda)
        }
    }
}
