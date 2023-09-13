import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './dodument.reducer';

export const DodumentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const dodumentEntity = useAppSelector(state => state.dodument.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="dodumentDetailsHeading">
          <Translate contentKey="odiseyDocApp.dodument.detail.title">Dodument</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{dodumentEntity.id}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="odiseyDocApp.dodument.description">Description</Translate>
            </span>
          </dt>
          <dd>{dodumentEntity.description}</dd>
          <dt>
            <span id="actionsDescription">
              <Translate contentKey="odiseyDocApp.dodument.actionsDescription">Actions Description</Translate>
            </span>
          </dt>
          <dd>{dodumentEntity.actionsDescription}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="odiseyDocApp.dodument.date">Date</Translate>
            </span>
          </dt>
          <dd>{dodumentEntity.date ? <TextFormat value={dodumentEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="odiseyDocApp.dodument.status">Status</Translate>
            </span>
          </dt>
          <dd>{dodumentEntity.status}</dd>
        </dl>
        <Button tag={Link} to="/dodument" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dodument/${dodumentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DodumentDetail;
