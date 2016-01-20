package cn.globalph.b2c.web.checkout.section;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public class CheckoutSectionDTO {

    protected CheckoutSectionViewType view;
    protected CheckoutSectionStateType state;
    protected boolean populated;
    protected String helpMessage;

    public CheckoutSectionDTO(CheckoutSectionViewType view, boolean populated) {
        this.view = view;
        this.populated = populated;
        this.state = CheckoutSectionStateType.INACTIVE;
    }

    public CheckoutSectionViewType getView() {
        return view;
    }

    public void setView(CheckoutSectionViewType view) {
        this.view = view;
    }

    public CheckoutSectionStateType getState() {
        return state;
    }

    public void setState(CheckoutSectionStateType state) {
        this.state = state;
    }

    public boolean isPopulated() {
        return populated;
    }

    public void setPopulated(boolean populated) {
        this.populated = populated;
    }

    public String getHelpMessage() {
        return helpMessage;
    }

    public void setHelpMessage(String helpMessage) {
        this.helpMessage = helpMessage;
    }
}
