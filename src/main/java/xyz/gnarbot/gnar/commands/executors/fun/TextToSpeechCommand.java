//package xyz.gnarbot.gnar.commands.executors.fun;
//
//import net.dv8tion.jda.core.MessageBuilder;
//import net.dv8tion.jda.core.Permission;
//import org.apache.commons.lang3.StringUtils;
//import xyz.gnarbot.gnar.commands.Category;
//import xyz.gnarbot.gnar.commands.Command;
//import xyz.gnarbot.gnar.commands.CommandExecutor;
//import xyz.gnarbot.gnar.commands.Scope;
//import xyz.gnarbot.gnar.utils.Context;
//
//@Command(
//        aliases = {"tts"},
//        usage = "(string)",
//        description = "Text to speech fun.",
//        category = Category.FUN,
//        scope = Scope.TEXT,
//        permissions = Permission.MESSAGE_TTS
//)
//public class TextToSpeechCommand extends CommandExecutor {
//    @Override
//    public void execute(Context context, String[] args) {
//        if (args.length == 0) {
//            context.send().error("Please provide a query.").queue();
//            return;
//        }
//
//        MessageBuilder builder = new MessageBuilder();
//        builder.setTTS(true);
//        builder.append(StringUtils.join(args, " "));
//
//        context.getMessage().getChannel().sendMessage(builder.build()).queue();
//    }
//}
