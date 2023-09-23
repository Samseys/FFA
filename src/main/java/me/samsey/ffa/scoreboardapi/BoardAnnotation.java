package me.samsey.ffa.scoreboardapi;

import org.bukkit.ChatColor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BoardAnnotation {
	
	String objName();

	String colorBefore() default "§c";

	String colorAfter() default "§e";

	String colorMid() default "§6";

	boolean bold() default true;

	ChatColor textColor() default ChatColor.GRAY;
	
	boolean upperCaseMid() default false;
}
