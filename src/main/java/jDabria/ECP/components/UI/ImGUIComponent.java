package jDabria.ECP.components.UI;

import commons.Utils;
import imgui.ImGui;
import jDabria.ECP.base.Component;
import jDabria.events.imGUI.IImGUIStartFrame;
import jDabria.imGUI.ImGUILayer;
import jDabria.imGUI.UIHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ImGUIComponent extends Component {
    private transient final IImGUIStartFrame renderEvent = this::internalRender;
    protected transient boolean useDefaultDrawer = false;

    //region Base class overrides
    @Override
    public void start(){
        ImGUILayer.addStartFrameListener(renderEvent);
    }

    @Override
    public void stop(){
        ImGUILayer.removeStartFrameListener(renderEvent);
    }
    //endregion

    private void internalRender(){
        ImGui.begin(gameObject.getName());
        if(useDefaultDrawer){
            Field[] fields = getClass().getDeclaredFields();
            for(Field field : fields){
                if(Modifier.isTransient(field.getModifiers())){
                    continue;
                }

                boolean wasPrivate = UIHelper.openIfPrivate(field);

                String fieldName = field.getName();
                Class<?> fieldType = field.getType();
                if(Utils.isIntOrFloat(fieldType) || Utils.isVector3f(fieldType)){
                    UIHelper.dragField(this, field, fieldName);
                }
                else if(Utils.isBoolean(fieldType)){
                    UIHelper.checkBox(this, field, fieldName);
                }


                UIHelper.restoreModifier(field, wasPrivate);
            }
        }


        render();
        ImGui.end();
    }

    /**
     * Override to implement custom rendering
     */
    protected void render(){}
}
