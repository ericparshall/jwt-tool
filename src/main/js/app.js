const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');


class App extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <JwtForm />
        );
    }
}

class JwtForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            notBeforeMinutes: -5,
            notAfterMinutes: 10,
            username: "",
            publicKey: "",
            privateKey: "",
            jwt: ""
        };
        this.getJwt = this.getJwt.bind(this);
        this.onChange = this.onChange.bind(this);
    }

    componentDidMount() {
        client({method: 'GET', path: '/api/generatekeys', headers: { 'Accept': 'application/json' }}).done(response => {
            var keys = response.entity;
            console.log(keys);
            this.setState({publicKey: keys.public, privateKey: keys.private});
        });
    }

    getJwt(event) {
        var data = {
            publicKey: this.state.publicKey,
            privateKey: this.state.privateKey,
            username: this.state.username,
            notBeforeMinutes: this.state.notBeforeMinutes,
            notAfterMinutes: this.state.notAfterMinutes
        };

        var Client = require('node-rest-client').Client;
        var restClient = new Client();
        var component = this;
        var baseUrl = window.location.protocol + '//' + window.location.host;
        restClient.post( baseUrl + '/api/generatejwt', { data: JSON.stringify(data), json: true, headers: { 'Accept': 'application/json', 'Content-Type': 'application/json' }}, function(data, response) {
            component.setState({ jwt: data.jwt });
        });
        event.preventDefault();
    }

    onChange(event) {
        var stateChange = {};
        stateChange[event.target.id] = event.target.value;
        this.setState(stateChange);
    }

    render() {
        var keyFieldStyle = {
            margin: "10px"
        };
        return (
            <form id="form_jwt-tool">
                <div className="row">
                    <div className="col-md-3">
                        <div style={keyFieldStyle} className="form-group">
                            <label htmlFor="publicKey">Public Key</label>
                            <textarea id="publicKey" value={this.state.publicKey} onChange={this.onChange}></textarea>
                        </div>
                    </div>
                    <div className="col-md-3">
                        <div style={keyFieldStyle} className="form-group">
                            <label htmlFor="privateKey">Private Key</label>
                            <textarea id="privateKey" value={this.state.privateKey} onChange={this.onChange}></textarea>
                        </div>
                    </div>
                </div>
                <div className="row">
                    <div className="col-md-3">
                        <div className="form-group" style={keyFieldStyle}>
                            <label htmlFor="notBeforeMinutes">Not Before Minutes from now</label>
                            <input id="notBeforeMinutes" type="number" value={this.state.notBeforeMinutes} onChange={this.onChange}/>
                        </div>
                    </div>
                    <div className="col-md-6">
                        <div className="form-group" style={keyFieldStyle}>
                            <label htmlFor="notAfterMinutes">Not After Minutes from now</label>
                            <input id="notAfterMinutes" type="number" value={this.state.notAfterMinutes} onChange={this.onChange}/>
                        </div>
                    </div>
                </div>
                <div className="row">
                    <div className="col-md-6">
                        <div className="form-group" style={keyFieldStyle}>
                            <label htmlFor="username">Username</label>
                            <input id="username" type="text" value={this.state.username} onChange={this.onChange}/>
                        </div>
                    </div>
                </div>
                <div className="row">
                    <div className="col-md-1">
                        <div className="form-group" style={keyFieldStyle}>
                            <button className="btn btn-default" onClick={this.getJwt}>Get JWT</button>
                        </div>
                    </div>
                    <div className="col-md-9">
                        <input type="text" value={this.state.jwt} style={{width: "100%"}} />
                    </div>
                </div>

            </form>
        )
    }
}

ReactDOM.render(
    <App />,
    document.getElementById('react')
)

