package com.drmangotea.createindustry.blocks.machines.oil_processing.distillation.output;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.FluidTank;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.List;

public class DistillationOutputBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {


    protected Storage<FluidVariant> fluidCapability;

    public final FluidTank tank = new SmartFluidTank(8000,this::onFluidStackChanged);
    public DistillationOutputBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        fluidCapability = tank.getSlot(0);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    protected void onFluidStackChanged(FluidStack newFluidStack) {
        if (!hasLevel())
            return;



        if (!level.isClientSide) {
            setChanged();
            sendData();
        }


    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        tank.readFromNBT(compound.getCompound("TankContent"));

    }


    @Override
    public void write(CompoundTag compound, boolean clientPacket) {


        compound.put("TankContent", tank.writeToNBT(new CompoundTag()));

    }
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {


        return containedFluidTooltip(tooltip, isPlayerSneaking,
                fluidCapability);
    }
}