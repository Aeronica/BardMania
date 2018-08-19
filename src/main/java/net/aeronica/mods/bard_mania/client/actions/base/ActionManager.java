/*
 * Copyright 2018 Paul Boese a.k.a Aeronica
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.aeronica.mods.bard_mania.client.actions.base;

import net.aeronica.mods.bard_mania.Reference;
import net.aeronica.mods.bard_mania.server.caps.BardActionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Reference.MOD_ID)
public class ActionManager
{
    private static Minecraft mc = Minecraft.getMinecraft();
    private static final ModelDummy modelDummy = new ModelDummy();
    private static List<ActionBase> actions =  new CopyOnWriteArrayList<>();
    private static float deltaTime = 0F;
    private static double total = 0F;
    private static float partialTicks = 0F;

    private ActionManager() {/* NOP */}

    public static void applyPose(EntityPlayer playerIn)
    {
        ModelDummy modelDummy = BardActionHelper.getModelDummy(playerIn);
        actions.add(new ApplyPose(playerIn, modelDummy));
    }

    public static void playAction(EntityPlayer playerIn, int noteIn)
    {
        ModelDummy modelDummy = BardActionHelper.getModelDummy(playerIn);
        actions.add(new PlayAction(playerIn, modelDummy, noteIn));
    }

    public static void equipAction(EntityPlayer playerIn)
    {
        ModelDummy modelDummy = BardActionHelper.getModelDummy(playerIn);
        actions.add(new EquipAction(playerIn, modelDummy));
    }

    public static void removeAction(EntityPlayer playerIn)
    {
        ModelDummy modelDummy = BardActionHelper.getModelDummy(playerIn);
        actions.add(new RemoveAction(playerIn, modelDummy));
    }

    public static ModelDummy getModelDummy(EntityPlayer playerIn)
    {
        return playerIn.hasCapability(Reference.BARD_ACTION_CAP, null) ? BardActionHelper.getModelDummy(playerIn) : modelDummy;
    }

    private static void update(float deltaTimeIn)
    {
        if (mc.world != null)
            actions.forEach(triggerAction -> triggerAction.update(deltaTimeIn));
    }

    private static void cleanup()
    {
        if (mc.world != null)
            actions.stream().filter(ActionBase::isDone).forEach(action -> actions.remove(action));
    }

    @SubscribeEvent
    public static void onTick(/*TickEvent.ClientTickEvent*/TickEvent.RenderTickEvent event)
    {
        if (event.phase.equals(TickEvent.Phase.START))
        {
            partialTicks = event.renderTickTime; // mc.getRenderPartialTicks();
            cleanup();
            return;
        }
        calcDelta();
        update(deltaTime);
    }

    private static void calcDelta()
    {
        if (mc.world != null)
        {
            double oldTotal = total;
            total = getElapsedWorldTime(partialTicks);
            deltaTime = (float) (total - oldTotal);
        }
    }

    public static double getElapsedWorldTime(float partialTicks)
    {
        WorldClient world = mc.world;
        long time = world != null ? world.getTotalWorldTime() : (long)total;
        return (time + partialTicks) / 20;
    }
}
