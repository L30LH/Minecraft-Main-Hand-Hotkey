package com.example.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.HumanoidArm;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

public class HandHotkeyModClient implements ClientModInitializer {
	private static KeyMapping toggleMainHandKey;

	@Override
	public void onInitializeClient() {
		toggleMainHandKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.example.toggle_main_hand",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_H,
				KeyMapping.Category.MISC
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (toggleMainHandKey.consumeClick()) {
				if (client.player != null && client.options != null) {
					HumanoidArm currentArm = client.options.mainHand().get();
					HumanoidArm newArm = currentArm == HumanoidArm.LEFT ? HumanoidArm.RIGHT : HumanoidArm.LEFT;
					client.options.mainHand().set(newArm);
					client.options.save();
					
					client.player.displayClientMessage(
						Component.translatable("options.mainHand").append(": ").append(Component.translatable("options.mainHand." + newArm.name().toLowerCase(java.util.Locale.ROOT))),
						true
					);
				}
			}
		});
	}
}
