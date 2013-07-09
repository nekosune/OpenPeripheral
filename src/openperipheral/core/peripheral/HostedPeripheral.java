package openperipheral.core.peripheral;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import openperipheral.core.AdapterManager;
import openperipheral.core.MethodDeclaration;
import openperipheral.core.interfaces.IAttachable;
import openperipheral.core.interfaces.IPeripheralMethodDefinition;

public class HostedPeripheral extends AbstractPeripheral {

	protected World worldObj;
	protected int x;
	protected int y;
	protected int z;

	public HostedPeripheral(TileEntity tile) {
		
		worldObj = tile.worldObj;
		x = tile.xCoord;
		y = tile.yCoord;
		z = tile.zCoord;
		
		Block blockType = tile.getBlockType();
		
		ItemStack is = new ItemStack(blockType, 1, blockType.getDamageValue(tile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord));

		try {
			name = is.getDisplayName();
		} catch (Exception e) {
			try {
				name = is.getItemName();
			} catch (Exception e2) {
			}
		}

		if (name == null) {
			name = tile.getClass().getName();
		}

		name = name.replace(".", "_");
		name = name.replace(" ", "_");
		name = name.toLowerCase();
	}
	
	@Override
	public IAttachable getAttachable() {
		Object target = getTarget();
		if (target instanceof IAttachable) {
			return (IAttachable) target;
		}
		return null;
	}

	@Override
	public World getWorldObject() {
		return worldObj;
	}

	@Override
	protected void replaceArguments(ArrayList<Object> args, HashMap<Integer, String> replacements) {
		if (replacements == null) {
			return;
		}
		for (Entry<Integer, String> replacement : replacements.entrySet()) {
			String r = replacement.getValue();
			Object v = null;
			if (r.equals("x")) {
				v = x;
			} else if (r.equals("y")) {
				v = y;
			} else if (r.equals("z")) {
				v = z;
			} else if (r.equals("world")) {
				v = getWorldObject();
			}
			if (v != null) {
				args.add(replacement.getKey(), v);
			}
		}
	}

	@Override
	public ArrayList<MethodDeclaration> getMethods() {
		return AdapterManager.getMethodsForTarget(getTarget());
	}

	public Object getTarget() {
		World worldObj = getWorldObject();
		TileEntity te = worldObj.getBlockTileEntity(x, y, z);
		return te;
	}
	
	@Override
	public Object getTargetObject(ArrayList args, IPeripheralMethodDefinition luaMethod) {
		return getTarget();
	}

}
