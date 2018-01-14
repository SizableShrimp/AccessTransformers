package cpw.mods.accesstransformer.transformer;

import cpw.mods.accesstransformer.*;
import cpw.mods.accesstransformer.parser.*;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;

public enum AccessTransformerEngine {
    INSTANCE;

    private AccessTransformerList masterList = new AccessTransformerList();

    public ClassNode transform(ClassNode clazzNode, final Type classType) {
        // this should never happen but safety first
        if (!masterList.containsClassTarget(classType)) {
            return clazzNode;
        }

        final Map<TargetType, List<AccessTransformer>> transformersForTarget = masterList.getTransformersForTarget(classType);
        transformersForTarget.forEach((tt, ats) -> {
            ats.stream().forEach(at-> at.applyModifier(clazzNode));
        });
        /*for (Modifier m : mods)
        {
            if (m.modifyClassVisibility)
            {
                classNode.access = getFixedAccess(classNode.access, m);
                if (DEBUG)
                {
                    FMLLog.log.debug("Class: {} {} -> {}", name, toBinary(m.oldAccess), toBinary(m.newAccess));
                }
                continue;
            }
            if (m.desc.isEmpty())
            {
                for (FieldNode n : classNode.fields)
                {
                    if (n.name.equals(m.name) || m.name.equals("*"))
                    {
                        n.access = getFixedAccess(n.access, m);
                        if (DEBUG)
                        {
                            FMLLog.log.debug("Field: {}.{} {} -> {}", name, n.name, toBinary(m.oldAccess), toBinary(m.newAccess));
                        }

                        if (!m.name.equals("*"))
                        {
                            break;
                        }
                    }
                }
            }
            else
            {
                List<MethodNode> nowOverrideable = Lists.newArrayList();
                for (MethodNode n : classNode.methods)
                {
                    if ((n.name.equals(m.name) && n.desc.equals(m.desc)) || m.name.equals("*"))
                    {
                        n.access = getFixedAccess(n.access, m);

                        // constructors always use INVOKESPECIAL
                        if (!n.name.equals("<init>"))
                        {
                            // if we changed from private to something else we need to replace all INVOKESPECIAL calls to this method with INVOKEVIRTUAL
                            // so that overridden methods will be called. Only need to scan this class, because obviously the method was private.
                            boolean wasPrivate = (m.oldAccess & ACC_PRIVATE) == ACC_PRIVATE;
                            boolean isNowPrivate = (m.newAccess & ACC_PRIVATE) == ACC_PRIVATE;

                            if (wasPrivate && !isNowPrivate)
                            {
                                nowOverrideable.add(n);
                            }

                        }

                        if (DEBUG)
                        {
                            FMLLog.log.debug("Method: {}.{}{} {} -> {}", name, n.name, n.desc, toBinary(m.oldAccess), toBinary(m.newAccess));
                        }

                        if (!m.name.equals("*"))
                        {
                            break;
                        }
                    }
                }

                replaceInvokeSpecial(classNode, nowOverrideable);
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
*/
        return null;
    }

    public void addResource(final Path path, final String resourceName) {
        try {
            masterList.loadFromPath(path, resourceName);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid path "+ path, e);
        }
    }

    public boolean handlesClass(final Type className) {
        return masterList.containsClassTarget(className);
    }
}
