package com.visaonoturna;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

public class VisaoNoturnaClient implements ClientModInitializer {
	public static final String MOD_ID = "visaonoturna";

	private static final KeyMapping.Category CATEGORIA = KeyMapping.Category.register(
			Identifier.fromNamespaceAndPath(MOD_ID, "main"));

	private static KeyMapping teclaAlternar;
	private static boolean ativo = false;

	// Referência ao efeito criado pelo mod, para não remover uma poção de verdade
	private static MobEffectInstance efeitoDoMod = null;

	@Override
	public void onInitializeClient() {
		// Tecla padrão: N (pode ser trocada em Opções > Controles > Atalhos de teclado)
		teclaAlternar = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.visaonoturna.toggle",
				InputConstants.Type.values()[0],
				InputConstants.KEY_N,
				CATEGORIA));

		ClientTickEvents.END_CLIENT_TICK.register(this::aoTick);
	}

	private void aoTick(Minecraft client) {
		LocalPlayer player = client.player;

		while (teclaAlternar.consumeClick()) {
			if (player == null) continue;
			ativo = !ativo;
			if (!ativo) {
				remover(player);
			}
			player.sendSystemMessage(Component.translatable(
					ativo ? "msg.visaonoturna.on" : "msg.visaonoturna.off"));
		}

		if (player == null) {
			efeitoDoMod = null;
			return;
		}

		// Reaplica sempre que o efeito sumir (morte, troca de dimensão, poção acabando etc.)
		if (ativo && !player.hasEffect(MobEffects.NIGHT_VISION)) {
			// duração infinita (sem piscar), sem partículas e sem ícone no HUD
			efeitoDoMod = new MobEffectInstance(
					MobEffects.NIGHT_VISION,
					MobEffectInstance.INFINITE_DURATION,
					0,
					false,
					false,
					false);
			player.addEffect(efeitoDoMod);
		}
	}

	private static void remover(LocalPlayer player) {
		MobEffectInstance atual = player.getEffect(MobEffects.NIGHT_VISION);
		if (atual != null && atual == efeitoDoMod) {
			player.removeEffect(MobEffects.NIGHT_VISION);
		}
		efeitoDoMod = null;
	}
}
