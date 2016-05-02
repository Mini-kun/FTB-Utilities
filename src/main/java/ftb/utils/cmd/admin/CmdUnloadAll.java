package ftb.utils.cmd.admin;

import ftb.lib.api.ForgePlayer;
import ftb.lib.api.ForgePlayerMP;
import ftb.lib.api.ForgeWorldMP;
import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;
import ftb.utils.world.ClaimedChunk;
import ftb.utils.world.FTBUWorldDataMP;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CmdUnloadAll extends CommandLM
{
	public CmdUnloadAll()
	{ super("unload_all", CommandLevel.OP); }
	
	@Override
	public String getCommandUsage(ICommandSender ics)
	{ return '/' + commandName + " <player | @a>"; }
	
	@Override
	public boolean isUsernameIndex(String[] args, int i)
	{ return i == 0; }
	
	@Override
	public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
	{
		checkArgs(args, 1);
		
		if(args[0].equals("@a"))
		{
			for(ClaimedChunk c : FTBUWorldDataMP.get().getAllChunks(null))
				c.isChunkloaded = false;
			for(ForgePlayer p : ForgeWorldMP.inst.getOnlinePlayers())
				p.toPlayerMP().sendUpdate();
			ics.addChatMessage(new TextComponentString("Unloaded all chunks"));
			return;
		}
		
		ForgePlayerMP p = ForgePlayerMP.get(args[0]);
		for(ClaimedChunk c : FTBUWorldDataMP.get().getChunks(p.getProfile().getId(), null))
			c.isChunkloaded = false;
		if(p.isOnline()) { p.sendUpdate(); }
		ics.addChatMessage(new TextComponentString("Unloaded all " + p.getProfile().getName() + "'s chunks"));
	}
}